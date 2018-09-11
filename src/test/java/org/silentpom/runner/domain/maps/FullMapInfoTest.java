package org.silentpom.runner.domain.maps;

import org.testng.annotations.Test;

import java.io.InputStream;
import java.io.InputStreamReader;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.testng.Assert.*;

/**
 * Created by Vlad on 09.09.2018.
 */
public class FullMapInfoTest {
    @Test
    public void testBuildFromMap() throws Exception {
        try (
                InputStream in = this.getClass().getClassLoader()
                        .getResourceAsStream("tableCorrect.txt");
                InputStreamReader reader = new InputStreamReader(in, UTF_8)) {
            SimpleMap simpleMap = SimpleMap.fromFile(reader);
            simpleMap.print();

            FullMapInfo info = FullMapInfo.buildFromMap(simpleMap);

            assertTrue(info.getGold().size() > 5);
            assertFalse(info.getEnemy().isEmpty());
            assertTrue(info.getEnemy().size() == 1);
            assertNotNull(info.getHero());
        }

    }
}