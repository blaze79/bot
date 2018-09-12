package org.silentpom.runner.algo.estimation;

import org.silentpom.runner.algo.estimation.policy.LinearWeightPolicy;
import org.silentpom.runner.algo.estimation.policy.WeightPolicy;
import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.maps.ClearMap;
import org.silentpom.runner.domain.maps.FullMapInfo;
import org.silentpom.runner.domain.maps.SimpleMap;
import org.silentpom.runner.domain.masks.DoubleMask;

import static org.silentpom.runner.domain.Constants.GOLD_COST;
import static org.silentpom.runner.domain.Constants.RATE_INFLATION;

/**
 * Created by Vlad on 12.09.2018.
 */
public class Estimator {

    public DoubleMask estimate(FullMapInfo position) {
        SimpleMap simpleMap = position.getSimple();
        ClearMap clearMap = position.getClearMap();
        DoubleMask mask = new DoubleMask(simpleMap.rows(), simpleMap.columns());

        for(Position gold: position.getGold()) {
            BackFiller filler = new BackFiller(
                    clearMap,
                    policy(),
                    position.getBots(),
                    position.getHero()
            );
            DoubleMask estimation = filler.estimation(gold);
            HeroBotHolder botHolder = filler.getHolder();

            if(botHolder.isHeroFound()) {
                mask.addWithWeight(
                        estimation,
                        reductionWeight(botHolder.getBotsFound())
                );
            }
        }

        return mask;
    }

    private WeightPolicy policy() {
        return POLICY;
    }

    private double reductionWeight(int order) {
        if (order == 0) {
            return 1;
        }
        return Math.pow(2, -order);
    }

    private static WeightPolicy POLICY = new LinearWeightPolicy(GOLD_COST, RATE_INFLATION);
}
