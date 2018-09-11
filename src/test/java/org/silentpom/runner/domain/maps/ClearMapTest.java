package org.silentpom.runner.domain.maps;

import org.testng.annotations.Test;

import java.io.InputStream;
import java.io.InputStreamReader;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.testng.Assert.*;

/**
 * Created by Vlad on 09.09.2018.
 */
public class ClearMapTest {
    @Test
    public void testFromMap() throws Exception {
        try (
                InputStream in = this.getClass().getClassLoader()
                        .getResourceAsStream("table.txt");
                InputStreamReader reader = new InputStreamReader(in, UTF_8)) {
            SimpleMap simpleMap = SimpleMap.fromFile(reader);

            ClearMap map = ClearMap.fromMap(simpleMap);
            map.checkCells();
            map.print();
        }
    }

}