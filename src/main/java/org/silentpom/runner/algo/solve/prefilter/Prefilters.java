package org.silentpom.runner.algo.solve.prefilter;

import org.silentpom.runner.algo.estimation.Estimator;
import org.silentpom.runner.algo.solve.commands.GameCommand;
import org.silentpom.runner.domain.maps.FullMapInfo;
import org.silentpom.runner.domain.masks.DoubleMask;

/**
 * Created by Vlad on 17.09.2018.
 */
public class Prefilters {
    SinglePrefilter[] prefilters = {
            new StayOnOnePositionFilter(),
            new CatchInHolePrefilter()
    };

    public GameCommand checkStupidSituations(Estimator estimator, FullMapInfo info) {
        for (SinglePrefilter prefilter : prefilters) {
            GameCommand gameCommand = prefilter.checkStupidSituations(estimator, info);
            if (gameCommand != null) {
                return gameCommand;
            }
        }
        return null;
    }

}
