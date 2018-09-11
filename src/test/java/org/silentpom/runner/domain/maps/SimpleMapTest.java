package org.silentpom.runner.domain.maps;

import org.testng.annotations.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.testng.Assert.*;

/**
 * Created by Vlad on 09.09.2018.
 */
public class SimpleMapTest {

    @Test
    public void testFromFile() throws Exception {
        try (
                InputStream in = this.getClass().getClassLoader()
                        .getResourceAsStream("table.txt");
                InputStreamReader reader = new InputStreamReader(in, UTF_8)) {
            SimpleMap simpleMap = SimpleMap.fromFile(reader);
            simpleMap.print();
        }
    }

}