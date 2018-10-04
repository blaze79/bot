package org.silentpom.runner.algo.solve.prefilter;

import org.silentpom.runner.algo.estimation.DirectFiller;
import org.silentpom.runner.algo.estimation.Estimator;
import org.silentpom.runner.algo.estimation.FillerResultHolder;
import org.silentpom.runner.algo.estimation.policy.LinearWeightPolicy;
import org.silentpom.runner.algo.estimation.policy.WeightPolicy;
import org.silentpom.runner.algo.solve.commands.DieCommand;
import org.silentpom.runner.algo.solve.commands.GameCommand;
import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.maps.FullMapInfo;
import org.silentpom.runner.utils.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Properties;

import static org.silentpom.runner.domain.Constants.GOLD_COST;
import static org.silentpom.runner.domain.Constants.RATE_INFLATION;

/**
 * Created by Vlad on 17.09.2018.
 */
public class CatchInHolePrefilter implements SinglePrefilter {
    public static Logger LOGGER = LoggerFactory.getLogger(CatchInHolePrefilter.class);
    int maxDeadArea = 6;
    int maxStepCount = 12;

    @Override
    public GameCommand checkStupidSituations(Estimator estimator, FullMapInfo info) {
        Position hero = info.getHero();
        if (hero == null) {
            return null;
        }

        DirectFiller filler = new DirectFiller(
                info.getClearMap(),
                LOCAL_POLICY,
                Collections.emptyList(),
                hero
        );

        FillerResultHolder estimation = filler.estimation(hero, maxStepCount);
        if (estimation.getMarks() < maxDeadArea) {
            LOGGER.warn("Area is too small {}. I hate myself and want to die", estimation.getMarks());
            return new DieCommand();
        }

        return null;
    }

    @Override
    public void reset() {

    }

    @Override
    public void readProperties(Properties properties) {
        maxDeadArea = PropertiesUtil.getValue(properties, "filter.hole.max-dead-area", maxDeadArea);
        maxStepCount = PropertiesUtil.getValue(properties, "filter.hole.max-step-count", maxStepCount);
    }

    private static WeightPolicy LOCAL_POLICY = new LinearWeightPolicy(GOLD_COST, RATE_INFLATION);
}
