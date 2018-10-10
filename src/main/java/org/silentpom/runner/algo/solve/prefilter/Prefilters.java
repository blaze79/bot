package org.silentpom.runner.algo.solve.prefilter;

import org.silentpom.runner.algo.estimation.Estimator;
import org.silentpom.runner.algo.solve.commands.GameCommand;
import org.silentpom.runner.domain.maps.FullMapInfo;
import org.silentpom.runner.domain.masks.DoubleMask;

import java.util.Properties;

/**
 * Created by Vlad on 17.09.2018.
 */
public class Prefilters {
    SinglePrefilter[] prefilters = {
            new ManiacPrefilter(),
            new StayOnOnePositionFilter(),
            new CatchInHolePrefilter(),
            new NoMoreGoldFilter()
    };

    public GameCommand checkStupidSituations(Estimator estimator, FullMapInfo info) {
        for (SinglePrefilter prefilter : prefilters) {
            GameCommand gameCommand = prefilter.checkStupidSituations(estimator, info);
            if (gameCommand != null) {
                reset();
                return gameCommand;
            }
        }
        return null;
    }

    public void takeResult(FullMapInfo info, GameCommand command, Estimator estimator, Estimator.Result result) {
        for (SinglePrefilter prefilter : prefilters) {
            prefilter.takeResult(info, command, estimator, result);
        }

        if(command.isDead()) {
            reset();
        }
    }

    public void reset() {
        for (SinglePrefilter prefilter : prefilters) {
            prefilter.reset();
        }
    }

    public void readProperties(Properties properties) {
        for (SinglePrefilter prefilter : prefilters) {
            prefilter.readProperties(properties);
        }
    }
}
