package org.silentpom.runner.algo.estimation.policy;

/**
 * Created by Vlad on 10.09.2018.
 */
public interface WeightPolicy {
    double startWeight();
    double reduceWeight(double w, int tick);
}
