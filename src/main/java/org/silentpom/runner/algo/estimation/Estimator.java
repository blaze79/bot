package org.silentpom.runner.algo.estimation;

import org.silentpom.runner.algo.estimation.policy.LinearWeightPolicy;
import org.silentpom.runner.algo.estimation.policy.WeightPolicy;
import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.maps.ClearMap;
import org.silentpom.runner.domain.maps.FullMapInfo;
import org.silentpom.runner.domain.maps.SimpleMap;
import org.silentpom.runner.domain.masks.DoubleMask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

import static org.silentpom.runner.domain.Constants.GOLD_COST;
import static org.silentpom.runner.domain.Constants.RATE_INFLATION;

/**
 * Created by Vlad on 12.09.2018.
 */
public class Estimator {

    public static Logger LOGGER = LoggerFactory.getLogger(Estimator.class);

    int depth = 5;
    int goldModeTime = depth + 1;

    int goldmodeCounter = 0;

    public DoubleMask estimate(FullMapInfo position) {
        SimpleMap simpleMap = position.getSimple();
        ClearMap clearMap = position.getClearMap();
        DoubleMask mask = new DoubleMask(simpleMap.rows(), simpleMap.columns());
        ArrayList<FillerResultHolder> goldenWays = new ArrayList<>();

        for (Position gold : position.getGold()) {
            BackFiller filler = new BackFiller(
                    clearMap,
                    policy(),
                    position.getBots(),
                    position.getHero()
            );

            FillerResultHolder botHolder = filler.estimation(gold);
            DoubleMask estimation = botHolder.getResult();

            if (botHolder.isHeroFound()) {
                mask.addWithWeight(
                        estimation,
                        reductionWeight(botHolder.getBotsFound())
                );
                goldenWays.add(botHolder);
            }
        }

        if (decreaseOneGoldMode() || mask.checkLocalMaximum(position.getHero())) {
            Optional<FillerResultHolder> min = goldenWays.stream().min(
                    Comparator.comparing(
                            result -> result.getHeroState().getGeneration()
                    )
            );

            if (min.isPresent()) {
                FillerResultHolder minHolder = min.get();
                int pathLen = minHolder.getHeroState().getGeneration();
                if (pathLen > depth) {
                    LOGGER.info("Player in local maximum and gold is too far {}", pathLen);
                    checkOneGoldMode();
                    return minHolder.getResult();
                } else {
                    LOGGER.debug("Player in local maximum but gold is near {}", pathLen);
                }
            }
        }

        return mask;
    }

    private boolean decreaseOneGoldMode() {
        if (isOneGoldMode()) {
            goldmodeCounter--;
            boolean mode = isOneGoldMode();
            if (mode == false) {
                LOGGER.info("Estimator switched back to MULTI mode");
            }
            return mode;
        }

        return false;
    }

    public boolean isOneGoldMode() {
        return goldmodeCounter > 0;
    }

    private void checkOneGoldMode() {
        if (goldmodeCounter == 0) {
            goldmodeCounter = this.goldModeTime;
            LOGGER.info("Estimator switched to ONE mode");
        }
    }

    public int getDepth() {
        return depth;
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
