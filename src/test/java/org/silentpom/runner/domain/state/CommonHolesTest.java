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

import org.silentpom.runner.domain.maps.FullMapInfo;
import org.silentpom.runner.domain.maps.SimpleMap;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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

            assertEquals(info.getHoles().size(), 5);
        }
    }
}