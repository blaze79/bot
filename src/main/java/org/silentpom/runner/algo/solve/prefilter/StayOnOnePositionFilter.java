package org.silentpom.runner.algo.solve.prefilter;

import org.silentpom.runner.algo.estimation.Estimator;
import org.silentpom.runner.algo.solve.commands.DieCommand;
import org.silentpom.runner.algo.solve.commands.GameCommand;
import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.maps.FullMapInfo;
import org.silentpom.runner.domain.state.PositionsCache;
import org.silentpom.runner.utils.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Created by Vlad on 17.09.2018.
 */
public class StayOnOnePositionFilter implements SinglePrefilter {
    private Position oldPosition = PositionsCache.make(-1, -1); // still correct
    private int counter = 0;
    private int gotoOneLimit = 5;
    private int deadLimit = 10;

    public static Logger LOGGER = LoggerFactory.getLogger(StayOnOnePositionFilter.class);

    @Override
    public GameCommand checkStupidSituations(Estimator estimator, FullMapInfo info) {
        Position hero = info.getHero();
        if (oldPosition.equals(hero)) {
            counter++;
            return processSamePosition(hero, estimator);
        } else {
            if (hero != null) {
                oldPosition = hero;
                reset();
            }
        }
        return null;
    }

    private GameCommand processSamePosition(Position hero, Estimator estimator) {
        if (counter == gotoOneLimit) {
            LOGGER.info("Estimator switched to SINGLE mode {}, {}", hero.getRow(), hero.getColumn());
            estimator.forceOneMode();
            return null;
        }

        if (counter >= deadLimit) {
            LOGGER.warn("Trapped in one point too long {}, {}", hero.getRow(), hero.getColumn());
            reset();
            return new DieCommand();
        }

        return null;
    }

    @Override
    public void reset() {
        counter = 0;
    }

    @Override
    public void readProperties(Properties properties) {
        gotoOneLimit = PropertiesUtil.getValue(properties, "filter.stay.one-gold-limit", gotoOneLimit);
        deadLimit = PropertiesUtil.getValue(properties, "filter.stay.reset-limit", deadLimit);
    }
}
