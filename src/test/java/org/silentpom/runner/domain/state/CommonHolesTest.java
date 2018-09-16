/*
 * Copyright 2018 Russian Post
 *
 * This source code is Russian Post Confidential Proprietary.
 * This software is protected by copyright. All rights and titles are reserved.
 * You shall not use, copy, distribute, modify, decompile, disassemble or reverse engineer the software.
 * Otherwise this violation would be treated by law and would be subject to legal prosecution.
 * Legal use of the software provides receipt of a license from the right holder only.
 */
package org.silentpom.runner.domain.state;

import org.silentpom.runner.domain.CellType;
import org.silentpom.runner.domain.actors.HoleCell;
import org.silentpom.runner.domain.maps.FullMapInfo;
import org.silentpom.runner.domain.maps.SimpleMap;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.testng.Assert.assertEquals;

public class CommonHolesTest {

    @Test
    public void testHoles() throws IOException {
        try (
                InputStream in = this.getClass().getClassLoader()
                        .getResourceAsStream("tableHoles.txt");
                InputStreamReader reader = new InputStreamReader(in, UTF_8)) {
            SimpleMap simpleMap = SimpleMap.fromFile(reader);
            simpleMap.print();

            FullMapInfo info = FullMapInfo.buildFromMap(simpleMap);

            List<HoleCell> mapHoles = info.getHoles();
            assertEquals(mapHoles.size(), 5);

            CommonHoles holes = new CommonHoles(mapHoles);

            CellType[] types = {CellType.DRILL_PIT, CellType.PIT_FILL_1, CellType.PIT_FILL_2, CellType.PIT_FILL_3, CellType.PIT_FILL_4};
            List<CellType> cellTypeList  = Stream.of(types).collect(Collectors.toList());

            for (int i = 0; i < cellTypeList.size(); ++i) {
                assertEquals(
                        holes.cellType(mapHoles.get(i).position(0)),
                        cellTypeList.get(i)
                );
            }

            holes.startNewTick();
            cellTypeList.remove(0);
            cellTypeList.add(null);

            for (int i = 0; i < cellTypeList.size(); ++i) {
                assertEquals(
                        holes.cellType(mapHoles.get(i).position(0)),
                        cellTypeList.get(i)
                );
            }

            holes.tickBack();
            cellTypeList  = Stream.of(types).collect(Collectors.toList());

            for (int i = 0; i < cellTypeList.size(); ++i) {
                assertEquals(
                        holes.cellType(mapHoles.get(i).position(0)),
                        cellTypeList.get(i)
                );
            }

        }
    }
}