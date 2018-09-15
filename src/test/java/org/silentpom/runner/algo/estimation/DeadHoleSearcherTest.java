package org.silentpom.runner.algo.estimation;

import org.silentpom.runner.domain.maps.FullMapInfo;
import org.silentpom.runner.domain.maps.SimpleMap;
import org.silentpom.runner.domain.masks.BitMask;
import org.silentpom.runner.domain.masks.DoubleMask;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.io.InputStreamReader;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.testng.Assert.*;

/**
 * Created by Vlad on 15.09.2018.
 */
public class DeadHoleSearcherTest {
    @Test
    public void testFindDeadHoles() throws Exception {
        try (
                InputStream in = this.getClass().getClassLoader()
                        .getResourceAsStream("tableDead.txt");
                InputStreamReader reader = new InputStreamReader(in, UTF_8)) {
            SimpleMap simpleMap = SimpleMap.fromFile(reader);
            simpleMap.print();

            FullMapInfo info = FullMapInfo.buildFromMap(simpleMap);

            DeadHoleSearcher holeSearcher = new DeadHoleSearcher();
            long time = System.currentTimeMillis();
            BitMask deadHoles = holeSearcher.findDeadHoles(info, 30);
            long usedTime = time - System.currentTimeMillis();

            System.out.printf("All holes found for %s ms\n", usedTime);

            deadHoles.print();

        }
    }

}