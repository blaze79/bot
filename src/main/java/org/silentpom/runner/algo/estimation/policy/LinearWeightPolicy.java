package org.silentpom.runner.algo.estimation.policy;

import org.silentpom.runner.domain.Constants;
import org.silentpom.runner.domain.Position;

/**
 * Created by Vlad on 10.09.2018.
 * was linear become exponential
 */

public class LinearWeightPolicy implements WeightPolicy {
    double weight;
    double rate = Constants.RATE_INFLATION;
    double altitudeRate = Constants.VERTICAL_INFLATION;

    public LinearWeightPolicy(double weight) {
        this.weight = weight;
    }

    public LinearWeightPolicy(double weight, double rate) {
        this.weight = weight;
        this.rate = rate;
    }


    @Override
    public double startWeight(Position pos) {
        return weight * Math.pow(altitudeRate, pos.getRow());
    }

    @Override
    public double reduceWeight(double w, int ticks) {
        return w * Math.pow(rate, ticks);
    }


}
