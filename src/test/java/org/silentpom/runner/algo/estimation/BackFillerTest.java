package org.silentpom.runner.algo.estimation;

import org.silentpom.runner.algo.estimation.policy.LinearWeightPolicy;
import org.silentpom.runner.algo.solve.commands.CommandResult;
import org.silentpom.runner.algo.solve.commands.GameCommand;
import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.maps.FullMapInfo;
import org.silentpom.runner.domain.maps.SimpleMap;
import org.silentpom.runner.replay.Replayer;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.testng.Assert.*;

/**
 * Created by Vlad on 11.09.2018.
 */
public class BackFillerTest {
    @Test
    public void testEstimation() throws Exception {
        try (
                InputStream in = this.getClass().getClassLoader()
                        .getResourceAsStream("tableCorrect.txt");
                InputStreamReader reader = new InputStreamReader(in, UTF_8)) {
            SimpleMap simpleMap = SimpleMap.fromFile(reader);
            simpleMap.print();

            FullMapInfo info = FullMapInfo.buildFromMap(simpleMap);

            LinearWeightPolicy policy = new LinearWeightPolicy(200);
            BackFiller filler = new BackFiller(
                    info.getClearMap(),
                    policy,
                    info.getBots(),
                    info.getHero()
            );
            FillerResultHolder resultHolder = filler.estimation(info.getGold().get(0));
            resultHolder.getMask().print();
            String field = resultHolder.getResult().getStringView();
            System.out.println(field);

            assertEquals(resultHolder.isHeroFound(), true);
            assertEquals(resultHolder.getBotsFound(), 1);

            resultHolder.getHeroState().print();

            List<FillerState> stateChain = resultHolder.getHeroState().asList();

            List<Position> directChain = new ArrayList<>();
            directChain.add(resultHolder.getHeroState().getPosition());

            CommandResult commandResult = new CommandResult();
            for (FillerState state : stateChain) {
                if(state.getCommand() == null) {
                    break;
                }
                commandResult.resetPosition(state.getPosition());
                GameCommand gameCommand = state.getCommand().toGameCommand();

                System.out.printf("Doing step %s\n", gameCommand.getCode());

                boolean res = gameCommand.moveInGame(info.getClearMap(), commandResult, false);
                assertTrue(res);

                directChain.add(commandResult.getPosition());
                assertEquals(state.getParent().getPosition(), commandResult.getPosition());
            }


            Replayer replayer = new Replayer();
            //replayer.replay(info.getClearMap(), stateChain);
            replayer.replayPositions(info.getClearMap(), directChain);
        }
    }

    @Test
    public void testEstimationLast() throws Exception {
        try (
                InputStream in = this.getClass().getClassLoader()
                        .getResourceAsStream("tableCorrect2.txt");
                InputStreamReader reader = new InputStreamReader(in, UTF_8)) {
            SimpleMap simpleMap = SimpleMap.fromFile(reader);
            simpleMap.print();

            FullMapInfo info = FullMapInfo.buildFromMap(simpleMap);

            LinearWeightPolicy policy = new LinearWeightPolicy(200);
            BackFiller filler = new BackFiller(
                    info.getClearMap(),
                    policy,
                    info.getBots(),
                    info.getHero()
            );
            FillerResultHolder resultHolder = filler.estimation(info.getGold().get(info.getGold().size() - 1));
            resultHolder.getMask().print();
            String field = resultHolder.getResult().getStringView();
            System.out.println(field);

            assertEquals(resultHolder.isHeroFound(), true);
            assertEquals(resultHolder.getBotsFound(), 0);

            resultHolder.getHeroState().print();

            Replayer replayer = new Replayer();
            replayer.replay(info.getClearMap(), resultHolder.getHeroState().asList());
        }
    }

    @Test
    public void testEstimationDig() throws Exception {
        try (
                InputStream in = this.getClass().getClassLoader()
                        .getResourceAsStream("tableCorrectDig.txt");
                InputStreamReader reader = new InputStreamReader(in, UTF_8)) {
            SimpleMap simpleMap = SimpleMap.fromFile(reader);
            simpleMap.print();

            FullMapInfo info = FullMapInfo.buildFromMap(simpleMap);

            LinearWeightPolicy policy = new LinearWeightPolicy(200);
            BackFiller filler = new BackFiller(
                    info.getClearMap(),
                    policy,
                    info.getBots(),
                    info.getHero()
            );
            FillerResultHolder resultHolder = filler.estimation(info.getGold().get(info.getGold().size() - 1));
            resultHolder.getMask().print();
            String field = resultHolder.getResult().getStringView();
            System.out.println(field);


            assertEquals(resultHolder.isHeroFound(), true);
            assertEquals(resultHolder.getBotsFound(), 0);

            resultHolder.getHeroState().print();

            Replayer replayer = new Replayer();
            replayer.replay(info.getClearMap(), resultHolder.getHeroState().asList());
        }
    }

    @Test
    public void testAnomaly() throws Exception {
        try (
                InputStream in = this.getClass().getClassLoader()
                        .getResourceAsStream("anomalyMap.txt");
                InputStreamReader reader = new InputStreamReader(in, UTF_8)) {
            SimpleMap simpleMap = SimpleMap.fromFile(reader);
            simpleMap.print();

            FullMapInfo info = FullMapInfo.buildFromMap(simpleMap);

            Estimator estimator = new Estimator();
            estimator.forceOneMode();
            estimator.estimate(info);

            List<FillerState> fillerStates = Estimator.BEST_SINGLE.getHeroState().asList();

            Replayer replayer = new Replayer();
            replayer.replay(info.getClearMap(), fillerStates);
        }
    }


    @Test
    public void testAnomaly2() throws Exception {
        try (
                InputStream in = this.getClass().getClassLoader()
                        .getResourceAsStream("anomalyMap2.txt");
                InputStreamReader reader = new InputStreamReader(in, UTF_8)) {
            SimpleMap simpleMap = SimpleMap.fromFile(reader);
            simpleMap.print();

            FullMapInfo info = FullMapInfo.buildFromMap(simpleMap);

            Estimator estimator = new Estimator();
            estimator.forceOneMode();
            estimator.estimate(info);

            FillerState heroState = Estimator.BEST_SINGLE.getHeroState();
            List<FillerState> fillerStates = heroState.asList();

            System.out.printf("Found solution steps %d  value %f step %s %n",
                    heroState.getGeneration(),
                    heroState.getValue(),
                    heroState.getCommand().getCode()
            );

            info.fakeHero(heroState.getParent().getPosition());
            estimator.estimate(info);
            heroState = Estimator.BEST_SINGLE.getHeroState();

            System.out.printf("Found solution steps %d  value %f step %s %n",
                    heroState.getGeneration(),
                    heroState.getValue(),
                    heroState.getCommand().getCode()
            );

            info.fakeHero(heroState.getParent().getPosition());
            estimator.estimate(info);
            heroState = Estimator.BEST_SINGLE.getHeroState();

            System.out.printf("Found solution steps %d  value %f step %s %n",
                    heroState.getGeneration(),
                    heroState.getValue(),
                    heroState.getCommand().getCode()
            );


            Replayer replayer = new Replayer();
            replayer.replay(info.getClearMap(), fillerStates);
        }
    }

}