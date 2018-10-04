/*
 * Copyright 2018 Russian Post
 *
 * This source code is Russian Post Confidential Proprietary.
 * This software is protected by copyright. All rights and titles are reserved.
 * You shall not use, copy, distribute, modify, decompile, disassemble or reverse engineer the software.
 * Otherwise this violation would be treated by law and would be subject to legal prosecution.
 * Legal use of the software provides receipt of a license from the right holder only.
 */
package org.silentpom.runner.algo.solve.prefilter;

import org.silentpom.runner.algo.estimation.Estimator;
import org.silentpom.runner.algo.solve.commands.DieCommand;
import org.silentpom.runner.algo.solve.commands.GameCommand;
import org.silentpom.runner.domain.CellType;
import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.maps.FullMapInfo;
import org.silentpom.runner.utils.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class NoMoreGoldFilter implements SinglePrefilter {
    int count = 0;
    int limit = 40;
    public static Logger LOGGER = LoggerFactory.getLogger(NoMoreGoldFilter.class);

    @Override
    public GameCommand checkStupidSituations(Estimator estimator, FullMapInfo info) {
        if (count >= limit) {
            Position hero = info.getHero();
            LOGGER.warn("No gold too long {}. Position {} {}", count, hero.getRow(), hero.getColumn());
            reset();
            return new DieCommand();
        }
        return null;
    }

    @Override
    public void takeResult(FullMapInfo info, GameCommand command) {
        Position newPosition = command.moveOnly(info.getHero());
        if (info.getClearMap().getCell(newPosition) == CellType.GOLD) {
            LOGGER.warn("Take GOLD after {}! at {}, {}", count, newPosition.getRow(), newPosition.getColumn());
            reset();
        } else {
            count++;
        }
    }

    @Override
    public void reset() {
        count = 0;
    }

    @Override
    public void readProperties(Properties properties) {
        limit = PropertiesUtil.getValue(properties, "filter.nogold.limit", limit);
    }
}
