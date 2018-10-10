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
import org.silentpom.runner.algo.solve.commands.DoNothingCommand;
import org.silentpom.runner.algo.solve.commands.GameCommand;
import org.silentpom.runner.domain.CellType;
import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.maps.FullMapInfo;
import org.silentpom.runner.utils.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class PushOutFormLocalMaxFilter implements SinglePrefilter {

    public static Logger LOGGER = LoggerFactory.getLogger(PushOutFormLocalMaxFilter.class);

    @Override
    public GameCommand checkStupidSituations(Estimator estimator, FullMapInfo info) {
        return null;
    }

    @Override
    public void takeResult(FullMapInfo info, GameCommand command, Estimator estimator, Estimator.Result result) {
        if(result.isHeroInMax()) {
            if(command instanceof DoNothingCommand) {
                estimator.forceOneMode();
                LOGGER.info("Hero wait in local maximum. Force single mode");
            }
        }
    }

    @Override
    public void reset() {
    }

    @Override
    public void readProperties(Properties properties) {

    }
}
