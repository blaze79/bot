package org.silentpom.runner.algo.solve;

import org.silentpom.runner.algo.estimation.Estimator;
import org.silentpom.runner.algo.solve.commands.GameCommand;
import org.silentpom.runner.domain.maps.FullMapInfo;

/**
 * Created by Vlad on 17.09.2018.
 */
public class GreedySolver implements ProblemSolver {
    @Override
    public GameCommand findBestCommand(Estimator.Result estimate, FullMapInfo info) {
        return estimate.bestCommand();
    }
}
