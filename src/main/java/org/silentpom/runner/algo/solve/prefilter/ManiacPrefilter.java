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
import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.maps.FullMapInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManiacPrefilter implements SinglePrefilter {
    int count = 0;
    int LIMIT = 30;

    public static Logger LOGGER = LoggerFactory.getLogger(ManiacPrefilter.class);

    @Override
    public GameCommand checkStupidSituations(Estimator estimator, FullMapInfo info) {
        Position hero = info.getHero();
        boolean near = info.getBots().stream().anyMatch(x -> x.absDistance(hero) <= 1);
        if (near) {
            count++;
            if (count >= LIMIT) {
                LOGGER.warn("Maniac too long {}, {}, time: ", hero.getRow(), hero.getColumn(), count);
                reset();
                return new DieCommand();

            }
        } else {
            reset();
        }

        return null;
    }

    @Override
    public void reset() {
        count = 0;
    }
}
