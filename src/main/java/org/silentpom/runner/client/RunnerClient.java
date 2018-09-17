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
import org.silentpom.runner.algo.solve.commands.GameCommand;
import org.silentpom.runner.domain.maps.FullMapInfo;
import org.silentpom.runner.domain.maps.SimpleMap;
import org.silentpom.runner.domain.masks.DoubleMask;
import org.silentpom.runner.replay.Replayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.silentpom.runner.algo.estimation.Estimator.BEST_SINGLE;

public class RunnerClient {
    String code = "14472771371957506935";
    String name = "vladislav.kogut@luxoft.com";

    String boardPatternStr = "^board=(.*)$";
    Pattern boardPattern = Pattern.compile(boardPatternStr);

    String url = "ws://loderunner.luxoft.com:8080/codenjoy-contest/ws?user=vladislav.kogut@luxoft.com&code=14472771371957506935";

    public static Logger LOGGER = LoggerFactory.getLogger(RunnerClient.class);

    public void startEndConnect() throws Exception {
        final GameClientEndpoint clientEndPoint = new GameClientEndpoint(new URI(url));
        clientEndPoint.addMessageHandler(new GameClientEndpoint.MessageHandler() {
            public void handleMessage(String message) {
                LOGGER.info(message);

                //clientEndPoint.sendMessage("RIGHT");
                String command = processStep(message);
                //command = "ACT LEFT";
                LOGGER.info("Command: {}", command);
                clientEndPoint.sendMessage(
                        command
                );

                /*JsonObject jsonObject = Json.createReader(new StringReader(message)).readObject();
                String userName = jsonObject.getString("user");
                if (!"bot".equals(userName)) {
                    clientEndPoint.sendMessage(getMessage("Hello " + userName +", How are you?"));
                    // other dirty bot logic goes here.. :)
                }*/
            }
        });

        while (true) {
            //clientEndPoint.sendMessage(getMessage("Hi There!!"));
            Thread.sleep(300000);
        }
    }

    public static void main(String[] args) throws Exception {
        RunnerClient client = new RunnerClient();
        client.startEndConnect();
    }

    private String processStep(String map) {
        Matcher matcher = boardPattern.matcher(map);
        if (!matcher.matches()) {
            LOGGER.error("PATTERN ERROR {}", map);
            return "LEFT";
        }

        SimpleMap simpleMap = SimpleMap.fromLongString(matcher.group(1));
        LOGGER.info(simpleMap.getStringView());


        FullMapInfo info = FullMapInfo.buildFromMap(simpleMap);
        Estimator estimator = new Estimator();

        // warm up

        long time = System.currentTimeMillis();
        estimator.forceOneMode();
        DoubleMask estimate = estimator.estimate(info);
        long usedTime = time - System.currentTimeMillis();

        LOGGER.info("Estimation done for {} ms", usedTime);

        if (BEST_SINGLE != null) {
            FillerState heroState = BEST_SINGLE.getHeroState();
            if (heroState != null) {
                GameCommand gameCommand = heroState.getCommand().toGameCommand();
                if (gameCommand != null) {
                    return gameCommand.getCode();
                }
            }
        }

        return "WAIT";
    }
}
