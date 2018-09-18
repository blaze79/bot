/*
 * Copyright 2018 Russian Post
 *
 * This source code is Russian Post Confidential Proprietary.
 * This software is protected by copyright. All rights and titles are reserved.
 * You shall not use, copy, distribute, modify, decompile, disassemble or reverse engineer the software.
 * Otherwise this violation would be treated by law and would be subject to legal prosecution.
 * Legal use of the software provides receipt of a license from the right holder only.
 */
package org.silentpom.runner.domain.maps;

import org.silentpom.runner.domain.CellCategory;
import org.silentpom.runner.domain.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MonsterGoldCorrector {
    List<Position> lastGold = Collections.emptyList();
    public static Logger LOGGER = LoggerFactory.getLogger(MonsterGoldCorrector.class);

    public void correctMonsterGold(FullMapInfo info) {
        for(Position gold: lastGold) {
            if(info.getSimple().getCell(gold).getCategory() == CellCategory.ENEMY) {
                info.getGold().add(gold);
                LOGGER.debug("Gold pos {} {} hided by hunter", gold.getRow(), gold.getColumn());
            }
        }

        lastGold = new ArrayList<>(info.getGold());
    }
}
