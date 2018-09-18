package org.silentpom.runner.algo.estimation.policy;

import org.silentpom.runner.domain.Position;

/**
 * Created by Vlad on 10.09.2018.
 */
public interface WeightPolicy {
    double startWeight(Position pos);
    double reduceWeight(double w, int tick);
}
