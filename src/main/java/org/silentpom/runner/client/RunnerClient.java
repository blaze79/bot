/*
 * Copyright 2018 Russian Post
 *
 * This source code is Russian Post Confidential Proprietary.
 * This software is protected by copyright. All rights and titles are reserved.
 * You shall not use, copy, distribute, modify, decompile, disassemble or reverse engineer the software.
 * Otherwise this violation would be treated by law and would be subject to legal prosecution.
 * Legal use of the software provides receipt of a license from the right holder only.
 */
package org.silentpom.runner.client;

import org.silentpom.runner.algo.estimation.Estimator;
import org.silentpom.runner.algo.estimation.FillerState;
import org.silentpom.runner.algo.solve.GreedySolver;
import org.silentpom.runner.algo.solve.OnlyHeroSolver;
import org.silentpom.runner.algo.solve.ProblemSolver;
import org.silentpom.runner.algo.solve.commands.DieCommand;
import org.silentpom.runner.algo.solve.commands.DoNothingCommand;
import org.silentpom.runner.algo.solve.commands.GameCommand;
import org.silentpom.runner.algo.solve.prefilter.Prefilters;
import org.silentpom.runner.domain.maps.FullMapInfo;
import org.silentpom.runner.domain.maps.MapDecoder;
import org.silentpom.runner.domain.maps.MonsterGoldCorrector;
import org.silentpom.runner.domain.maps.SimpleMap;
import org.silentpom.runner.domain.masks.DoubleMask;
import org.silentpom.runner.replay.Replayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.silentpom.runner.algo.estimation.Estimator.BEST_SINGLE;

public class RunnerClient {
    String url = "ws://loderunner.luxoft.com:8080/codenjoy-contest/ws?user=vladislav.kogut@luxoft.com&code=14472771371957506935";

    public static Logger LOGGER = LoggerFactory.getLogger(RunnerClient.class);

    private MapDecoder mapDecoder = new MapDecoder();
    private Estimator estimator = new Estimator();
    //ProblemSolver solver = new GreedySolver();
    private ProblemSolver solver = new OnlyHeroSolver(9);
    private Prefilters prefilters = new Prefilters();
    private MonsterGoldCorrector goldCorrector = new MonsterGoldCorrector();

    public void startEndConnect() throws Exception {
        Properties props = new Properties();
        try (InputStream stream = this.getClass().getResourceAsStream("/runner.properties")) {
            props.load(stream);
        }
        url = props.getProperty("runner.server.url");
        prefilters.readProperties(props);

        final GameClientEndpoint clientEndPoint = new GameClientEndpoint(new URI(url));
        clientEndPoint.addMessageHandler(new GameClientEndpoint.MessageHandler() {

            public void handleMessage(String message) {
                String command = "WAIT";
                try {
                    FullMapInfo info = mapDecoder.mapDecode(message);
                    command = processStep(info);
                } catch (Exception ex) {
                    LOGGER.error("Exception ", ex);
                }

                LOGGER.info("Command: {}", command);
                clientEndPoint.sendMessage(
                        command
                );
            }
        });

        while (true) {
            Thread.sleep(5000);
        }
    }

    public static void main(String[] args) throws Exception {
        RunnerClient client = new RunnerClient();
        client.startEndConnect();
    }

    private String processStep(FullMapInfo info) {
        goldCorrector.correctMonsterGold(info);

        long time = System.currentTimeMillis();
        // TODO: check it
        estimator.forceOneMode();
        DoubleMask estimate = estimator.estimate(info);
        long usedTime = time - System.currentTimeMillis();

        LOGGER.info("Estimation done for {} ms", usedTime);

        try {
            if (BEST_SINGLE.getHeroState().getGeneration() > 25) {
                prefilters.reset();
                return new DieCommand().getCode();
            }
        } catch (Exception ex) {

        }

        GameCommand preCommand = prefilters.checkStupidSituations(estimator, info);
        if (preCommand != null) {
            return preCommand.getCode();
        }

        time = System.currentTimeMillis();
        GameCommand bestCommand = solver.findBestCommand(estimate, info);
        usedTime = time - System.currentTimeMillis();
        LOGGER.info("Solve done for {} ms", usedTime);

        if (bestCommand != null) {
            prefilters.takeResult(info, bestCommand);
            return bestCommand.getCode();
        }

        prefilters.takeResult(info, new DoNothingCommand());
        return "WAIT";
    }
}
