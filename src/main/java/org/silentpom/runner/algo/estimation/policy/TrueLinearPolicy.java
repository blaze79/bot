package org.silentpom.runner.algo.estimation.policy;

import org.silentpom.runner.domain.Constants;
import org.silentpom.runner.domain.Position;

/**
 * Created by Vlad on 10.09.2018.
 * was linear become exponential
 */

public class TrueLinearPolicy implements WeightPolicy {
    double weight;
    double delta;

    public TrueLinearPolicy(double weight, int steps) {
        this.weight = weight;
        this.delta = weight / (steps + 1);
    }


    @Override
    public double startWeight(Position pos) {
        return weight;
    }

    @Override
    public double reduceWeight(double w, int ticks) {
        return Math.max(w - delta, 0);
    }
}
