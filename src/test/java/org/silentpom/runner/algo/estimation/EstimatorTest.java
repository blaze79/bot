package org.silentpom.runner.algo.estimation;

import org.silentpom.runner.domain.CellType;
import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.maps.FullMapInfo;
import org.silentpom.runner.domain.maps.SimpleMap;
import org.silentpom.runner.domain.masks.DoubleMask;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

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


    @Test
    public void testEstimateMaximum() throws Exception {
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

            List<Position> maximum = estimation.findMaximum();
            for (Position pos : maximum) {
                CellType cellType = simpleMap.getCell(pos);
                System.out.printf("max %s: row %d col %d weight %f\n", cellType, pos.getRow(), pos.getColumn(), estimation.getChecked(pos));
                if (cellType != cellType.GOLD) {
                    FullMapInfo newInfo = FullMapInfo.buildFromMap(simpleMap);
                    newInfo.fakeHero(pos);

                    Estimator newEstimator = new Estimator();
                    DoubleMask newEstimation = newEstimator.estimate(newInfo);
                    if (newEstimator.isOneGoldMode()) {
                        System.out.println("New estimation map looks like");
                        System.out.println(newEstimation.getStringView());
                    }
                }
            }
        }
    }

}