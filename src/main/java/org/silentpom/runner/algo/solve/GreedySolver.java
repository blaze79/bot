package org.silentpom.runner.algo.solve;

import org.silentpom.runner.algo.estimation.FillerState;
import org.silentpom.runner.algo.solve.commands.GameCommand;
import org.silentpom.runner.domain.maps.FullMapInfo;
import org.silentpom.runner.domain.masks.DoubleMask;

import static org.silentpom.runner.algo.estimation.Estimator.BEST_SINGLE;

/**
 * Created by Vlad on 17.09.2018.
 */
public class GreedySolver implements ProblemSolver {
    @Override
    public GameCommand findBestCommand(DoubleMask estimate, FullMapInfo info) {
        if (BEST_SINGLE != null) {
            FillerState heroState = BEST_SINGLE.getHeroState();
            if (heroState != null) {
                return heroState.getCommand().toGameCommand();
            }
        }
        return null;
    }
}
