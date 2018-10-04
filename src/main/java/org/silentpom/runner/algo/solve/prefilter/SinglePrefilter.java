package org.silentpom.runner.algo.solve.prefilter;

import org.silentpom.runner.algo.estimation.Estimator;
import org.silentpom.runner.algo.solve.commands.GameCommand;
import org.silentpom.runner.domain.maps.FullMapInfo;

import java.util.Properties;

/**
 * Created by Vlad on 17.09.2018.
 */
public interface SinglePrefilter {

    GameCommand checkStupidSituations(Estimator estimator, FullMapInfo info);

    void reset();

    default void takeResult(FullMapInfo info, GameCommand command) {

    };

    void readProperties(Properties properties);
}
