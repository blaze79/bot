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
import org.silentpom.runner.domain.Position;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertEquals;

public class HolesOfActorTest {
    @Test
    public void testHoles() throws IOException {
        Position pos = PositionsCache.make(10, 11);
        Position pos2 = PositionsCache.make(10, 12);

        HolesOfActor holesOfActor = new HolesOfActor();

        holesOfActor.startNewTick(pos);
        holesOfActor.startNewTick(null);

        //assertEquals(holesOfActor.cellType(pos), CellType.PIT_FILL_1);
        assertEquals(holesOfActor.cellType(pos), CellType.PIT_FILL_8);
        holesOfActor.startNewTick(pos2);

        //assertEquals(holesOfActor.cellType(pos), CellType.PIT_FILL_2);
        assertEquals(holesOfActor.cellType(pos), CellType.PIT_FILL_8);
        assertEquals(holesOfActor.cellType(pos2), CellType.PIT_FILL_8);

        holesOfActor.startNewTick(null);
        assertEquals(holesOfActor.cellType(pos), CellType.PIT_FILL_8);

        holesOfActor.startNewTick(null);
        assertEquals(holesOfActor.cellType(pos), CellType.PIT_FILL_8);

        holesOfActor.startNewTick(null);
        assertEquals(holesOfActor.cellType(pos), CellType.PIT_FILL_8);

        for(int i=0; i<6; ++i) {
            holesOfActor.tickBack();
        }
        assertEquals(holesOfActor.cellType(pos), null);
    }
}
