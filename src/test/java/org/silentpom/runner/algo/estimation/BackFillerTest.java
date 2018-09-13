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
            filler.estimation(info.getGold().get(0));
            filler.getMask().print();
            String field = filler.getResult().getStringView();
            System.out.println(field);

            assertEquals(filler.getHolder().isHeroFound(), true);
            assertEquals(filler.getHolder().getBotsFound(), 1);

            filler.getHolder().getHeroState().print();

            List<FillerState> stateChain = filler.getHolder().getHeroState().asList();

            List<Position> directChain = new ArrayList<>();
            directChain.add(filler.getHolder().getHeroState().getPosition());

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
            filler.estimation(info.getGold().get(info.getGold().size() - 1));
            filler.getMask().print();
            String field = filler.getResult().getStringView();
            System.out.println(field);

            assertEquals(filler.getHolder().isHeroFound(), true);
            assertEquals(filler.getHolder().getBotsFound(), 0);

            filler.getHolder().getHeroState().print();

            Replayer replayer = new Replayer();
            replayer.replay(info.getClearMap(), filler.getHolder().getHeroState().asList());
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
            filler.estimation(info.getGold().get(info.getGold().size() - 1));
            filler.getMask().print();
            String field = filler.getResult().getStringView();
            System.out.println(field);


            assertEquals(filler.getHolder().isHeroFound(), true);
            assertEquals(filler.getHolder().getBotsFound(), 0);

            filler.getHolder().getHeroState().print();

            Replayer replayer = new Replayer();
            replayer.replay(info.getClearMap(), filler.getHolder().getHeroState().asList());
        }
    }

}