package org.silentpom.runner.domain.commands;

import org.silentpom.runner.algo.estimation.commands.MoveCommandsSet;
import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.maps.FullMapInfo;
import org.silentpom.runner.domain.maps.SimpleMap;
import org.silentpom.runner.domain.state.PositionsCache;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.testng.Assert.*;

/**
 * Created by Vlad on 10.09.2018.
 */
public class MoveCommandsSetTest {
    @Test
    public void testGetPreviousPoints() throws Exception {
        try (
                InputStream in = this.getClass().getClassLoader()
                        .getResourceAsStream("tableCorrect.txt");
                InputStreamReader reader = new InputStreamReader(in, UTF_8)) {
            SimpleMap simpleMap = SimpleMap.fromFile(reader);
            simpleMap.print();

            FullMapInfo info = FullMapInfo.buildFromMap(simpleMap);

            List<Position> hero = MoveCommandsSet.MOVE_COMMANDS.getPreviousPoints(info.getHero(), info.getClearMap());
            assertTrue(hero.size() == 4);

            List<Position> enemy = MoveCommandsSet.MOVE_COMMANDS.getPreviousPoints(info.getEnemy().get(0), info.getClearMap());
            assertTrue(enemy.size() == 3);

            List<Position> bots = MoveCommandsSet.MOVE_COMMANDS.getPreviousPoints(info.getBots().get(0), info.getClearMap());
            assertTrue(bots.size() == 2);

            List<Position> empty = MoveCommandsSet.MOVE_COMMANDS.getPreviousPoints(
                    PositionsCache.make(3, 27),
                    info.getClearMap()
            );
            assertTrue(empty.size() == 1);

        }
    }

}