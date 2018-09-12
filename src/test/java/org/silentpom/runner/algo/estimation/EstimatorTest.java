package org.silentpom.runner.algo.estimation;

import org.silentpom.runner.domain.maps.FullMapInfo;
import org.silentpom.runner.domain.maps.SimpleMap;
import org.silentpom.runner.domain.masks.DoubleMask;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.io.InputStreamReader;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.testng.Assert.*;

/**
 * Created by Vlad on 12.09.2018.
 */
public class EstimatorTest {
    @Test
    public void testEstimate() throws Exception {
        try (
                InputStream in = this.getClass().getClassLoader()
                        .getResourceAsStream("tableCorrect.txt");
                InputStreamReader reader = new InputStreamReader(in, UTF_8)) {
            SimpleMap simpleMap = SimpleMap.fromFile(reader);
            simpleMap.print();

            FullMapInfo info = FullMapInfo.buildFromMap(simpleMap);

            Estimator estimator = new Estimator();
            DoubleMask estimation = estimator.estimate(info);

            System.out.println(estimation.getStringView());
        }
    }

}