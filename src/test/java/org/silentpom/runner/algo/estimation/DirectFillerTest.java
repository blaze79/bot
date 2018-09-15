package org.silentpom.runner.algo.estimation;

import org.silentpom.runner.algo.estimation.policy.LinearWeightPolicy;
import org.silentpom.runner.domain.maps.FullMapInfo;
import org.silentpom.runner.domain.maps.SimpleMap;
import org.silentpom.runner.domain.state.PositionsCache;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.io.InputStreamReader;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.testng.Assert.assertEquals;

/**
 * Created by Vlad on 15.09.2018.
 */
public class DirectFillerTest {
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

            DirectFiller filler = new DirectFiller(
                    info.getClearMap(),
                    policy,
                    info.getBots(),
                    info.getHero()
            );

            FillerResultHolder resultHolder = filler.estimation(
                    PositionsCache.make(14, 22),
                    4
            );
            resultHolder.getMask().print();


            assertEquals(resultHolder.getMarks() > 5, true);
        }
    }

}