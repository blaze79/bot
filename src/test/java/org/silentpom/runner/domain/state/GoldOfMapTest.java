package org.silentpom.runner.domain.state;

import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.maps.FullMapInfo;
import org.silentpom.runner.domain.maps.SimpleMap;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.io.InputStreamReader;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.testng.Assert.*;

/**
 * Created by Vlad on 16.09.2018.
 */
public class GoldOfMapTest {
    /*@Test
    public void testCheckGold() throws Exception {
        try (
                InputStream in = this.getClass().getClassLoader()
                        .getResourceAsStream("tableHoles.txt");
                InputStreamReader reader = new InputStreamReader(in, UTF_8)) {
            SimpleMap simpleMap = SimpleMap.fromFile(reader);
            simpleMap.print();

            FullMapInfo info = FullMapInfo.buildFromMap(simpleMap);

            GoldOfMap goldOfMap = new GoldOfMap(info, 2);

            Position gold1 = PositionsCache.make(1, 11);
            Position gold2 = PositionsCache.make(3, 4);
            Position gold3 = PositionsCache.make(3, 19);

            assertTrue(goldOfMap.checkGold(gold1));
            assertTrue(goldOfMap.checkGold(gold2));
            assertTrue(goldOfMap.checkGold(gold3));

            Position bot1 = gold1.left().left();
            Position bot2 = gold2.left();

            goldOfMap.newTick();
            assertFalse(goldOfMap.objectMoved(0, bot1.right()));
            assertTrue(goldOfMap.objectMoved(1, bot2.right()));

            assertTrue(goldOfMap.checkGold(gold1));
            assertFalse(goldOfMap.checkGold(gold2));
            assertTrue(goldOfMap.checkGold(gold3));


            goldOfMap.newTick();
            assertTrue(goldOfMap.objectMoved(0, bot1.right().right()));
            assertFalse(goldOfMap.objectMoved(1, bot2.right()));

            assertFalse(goldOfMap.checkGold(gold1));
            assertFalse(goldOfMap.checkGold(gold2));
            assertTrue(goldOfMap.checkGold(gold3));

            goldOfMap.objectMoved(0, null);

            assertTrue(goldOfMap.checkGold(gold1));
            assertFalse(goldOfMap.checkGold(gold2));
            assertTrue(goldOfMap.checkGold(gold3));
        }
    }*/

}