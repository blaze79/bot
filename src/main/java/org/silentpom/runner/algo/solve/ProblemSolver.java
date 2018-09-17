package org.silentpom.runner.algo.solve;

import org.silentpom.runner.algo.solve.commands.GameCommand;
import org.silentpom.runner.domain.maps.FullMapInfo;
import org.silentpom.runner.domain.masks.DoubleMask;

/**
 * Created by Vlad on 17.09.2018.
 */
public interface ProblemSolver {
    GameCommand findBestCommand(DoubleMask estimate, FullMapInfo info);
}
