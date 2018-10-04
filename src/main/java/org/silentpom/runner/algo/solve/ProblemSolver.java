package org.silentpom.runner.algo.solve;

import org.silentpom.runner.algo.estimation.Estimator;
import org.silentpom.runner.algo.solve.commands.GameCommand;
import org.silentpom.runner.domain.maps.FullMapInfo;

/**
 * Created by Vlad on 17.09.2018.
 */
public interface ProblemSolver {
    GameCommand findBestCommand(Estimator.Result estimate, FullMapInfo info);
}
