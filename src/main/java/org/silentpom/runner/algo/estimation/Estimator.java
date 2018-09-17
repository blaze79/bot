package org.silentpom.runner.algo.estimation;

import javafx.geometry.Pos;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.silentpom.runner.domain.Constants.GOLD_COST;
import static org.silentpom.runner.domain.Constants.RATE_INFLATION;

/**
 * Created by Vlad on 12.09.2018.
 */
public class Estimator {

    public static Logger LOGGER = LoggerFactory.getLogger(Estimator.class);

    int depth = 5;
    int fillDepth = 4;
    int goldModeTime = depth + 1;

    int goldmodeCounter = 0;

    int WINDOW = 30;
    int closeGold = 2;

    public DoubleMask estimate(FullMapInfo position) {
        SimpleMap simpleMap = position.getSimple();
        ClearMap clearMap = position.getClearMap();
        DoubleMask mask = new DoubleMask(simpleMap.rows(), simpleMap.columns());
        ArrayList<FillerResultHolder> goldenWays = new ArrayList<>();

        List<Position> usedGold = filterGolds(position.getGold(), position.getHero());

        for (Position gold : usedGold) {
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

        if (decreaseOneGoldMode() || checkLocalMaximum(position, mask, position.getHero())) {
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
                    BEST_SINGLE = minHolder;
                    return minHolder.getResult();
                } else {
                    LOGGER.debug("Player in local maximum but gold is near {}", pathLen);

                    BEST_SINGLE = minHolder;
                }
            }
        }

        return mask;
    }

    private boolean checkLocalMaximum(FullMapInfo fullMapInfo, DoubleMask mask, Position pos) {
        DirectFiller filler = new DirectFiller(
                fullMapInfo.getClearMap(),
                policy(),
                fullMapInfo.getBots(),
                fullMapInfo.getHero()
        );

        FillerResultHolder estimation = filler.estimation(pos, fillDepth);
        Stream<Position> area = estimation.getMarkedPoints().stream().skip(1);
        double maxValue = mask.findMaximumOfArea(area);

        return mask.getChecked(pos) > maxValue;
        //mask.checkLocalMaximum(position.getHero())

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

    private List<Position> filterGolds(List<Position> golds, Position hero) {
        long nearGold = golds.stream().filter(gold -> distance(gold, hero) < WINDOW).count();
        if (nearGold <= closeGold) {
            return golds;
        }

        return golds.stream().filter(gold -> distance(gold, hero) < WINDOW).collect(Collectors.toList());
    }

    private int distance(Position a, Position b) {
        return Math.abs(a.getRow() - b.getRow()) + Math.abs(a.getColumn() - b.getColumn());
    }

    public void forceOneMode() {
        goldmodeCounter = 10;
    }

    private static WeightPolicy POLICY = new LinearWeightPolicy(GOLD_COST, RATE_INFLATION);

    public static  FillerResultHolder BEST_SINGLE = null;
}
