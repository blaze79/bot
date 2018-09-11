package org.silentpom.runner.algo.estimation;

import org.silentpom.runner.algo.estimation.policy.LinearWeightPolicy;
import org.silentpom.runner.domain.maps.FullMapInfo;
import org.silentpom.runner.domain.maps.SimpleMap;
import org.silentpom.runner.replay.Replayer;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.io.InputStreamReader;

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

            assertEquals( filler.getHolder().isHeroFound(), true);
            assertEquals( filler.getHolder().getBotsFound(), 1);

            filler.getHolder().getHeroState().print();

            Replayer replayer = new Replayer();
            replayer.replay(info.getClearMap(), filler.getHolder().getHeroState().asList());
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
            filler.estimation(info.getGold().get(info.getGold().size()-1));
            filler.getMask().print();

            assertEquals( filler.getHolder().isHeroFound(), true);
            assertEquals( filler.getHolder().getBotsFound(), 0);

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
            filler.estimation(info.getGold().get(info.getGold().size()-1));
            filler.getMask().print();

            assertEquals( filler.getHolder().isHeroFound(), true);
            assertEquals( filler.getHolder().getBotsFound(), 0);

            filler.getHolder().getHeroState().print();

            Replayer replayer = new Replayer();
            replayer.replay(info.getClearMap(), filler.getHolder().getHeroState().asList());
        }
    }

}