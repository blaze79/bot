package org.silentpom.runner.algo.estimation;

import org.silentpom.runner.algo.estimation.policy.LinearWeightPolicy;
import org.silentpom.runner.algo.estimation.policy.WeightPolicy;
import org.silentpom.runner.algo.solve.commands.DoNothingCommand;
import org.silentpom.runner.algo.solve.commands.GameCommand;
import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.maps.ClearMap;
import org.silentpom.runner.domain.maps.FullMapInfo;
import org.silentpom.runner.domain.maps.SimpleMap;
import org.silentpom.runner.domain.masks.DoubleMask;
import org.silentpom.runner.utils.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.silentpom.runner.domain.Constants.GOLD_COST;
import static org.silentpom.runner.domain.Constants.RATE_INFLATION;

/**
 * Created by Vlad on 12.09.2018.
 */
public class Estimator {

    public static Logger LOGGER = LoggerFactory.getLogger(Estimator.class);

    //int depth = 5;

    // how many steps solver can take
    int fillDepth = 6;

    // how many steps be in one gold mode
    int goldModeTime = fillDepth + 10;

    int goldmodeCounter = 0;
    int goldmodeCounterForced = 20;

    int window = 40;
    int closeGoldLimit = 2;

    public DoubleMask estimateOld(FullMapInfo position) {
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
//            Optional<FillerResultHolder> min = goldenWays.stream().min(
//                    Comparator.comparing(
//                            result -> result.getHeroState().getGeneration()
//                    )
//            );

            Optional<FillerResultHolder> min = goldenWays.stream().max(
                    Comparator.comparing(
                            result -> result.getHeroState().getValue()
                    )
            );

            if (min.isPresent()) {
                FillerResultHolder minHolder = min.get();
                int pathLen = minHolder.getHeroState().getGeneration();

                LOGGER.info("Player in local maximum and gold distance is  {}", pathLen);
                checkOneGoldMode();
                BEST_SINGLE = minHolder;
                return minHolder.getResult();

                /*if (pathLen > depth) {
                    LOGGER.info("Player in local maximum and gold is too far {}", pathLen);
                    checkOneGoldMode();
                    BEST_SINGLE = minHolder;
                    return minHolder.getResult();
                } else {
                    LOGGER.debug("Player in local maximum but gold is near {}", pathLen);
                    // TODO: return breaks algorithm?
                }*/
            }
        }

        return mask;
    }

    public Result estimate(FullMapInfo position) {
        SimpleMap simpleMap = position.getSimple();
        ClearMap clearMap = position.getClearMap();
        DoubleMask mask = new DoubleMask(simpleMap.rows(), simpleMap.columns());
        ArrayList<FillerResultHolder> goldenWays = new ArrayList<>();

        List<Position> usedGold = filterGolds(position.getGold(), position.getHero());

        Result fullEstimation = new Result();

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

        fullEstimation.setMask(mask);

        Optional<FillerResultHolder> min = goldenWays.stream().max(
                Comparator.comparing(
                        result -> result.getHeroState().getValue()
                )
        );

        if (min.isPresent()) {
            FillerResultHolder minHolder = min.get();
            int pathLen = minHolder.getHeroState().getGeneration();
            fullEstimation.bestGold(minHolder);

            if (decreaseOneGoldMode() || checkLocalMaximum(position, mask, position.getHero())) {
                checkOneGoldMode();
                fullEstimation.setMask(minHolder.getResult());

                LOGGER.info("Player in local maximum/closest mode and gold distance is {}", pathLen);

            } else {
                LOGGER.info("Player in multigold mode and gold distance is {}", pathLen);
            }
        } else {
            LOGGER.info("No gold found in window {}", window);
        }

        return fullEstimation;
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

        return mask.getChecked(pos) >= maxValue;
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

//    public int getDepth() {
//        return depth;
//    }

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
        long nearGold = golds.stream().filter(gold -> distance(gold, hero) < window).count();
        if (nearGold <= closeGoldLimit) {
            LOGGER.debug("NO GOLD IN WINDOW {}, full scan", window);
            return golds;
        }

        return golds.stream().filter(gold -> distance(gold, hero) < window).collect(Collectors.toList());
    }

    private int distance(Position a, Position b) {
        return a.absDistance(b);
    }

    public void forceOneMode() {
        goldmodeCounter = goldmodeCounterForced;
    }

    public void readProperties(Properties properties) {
        fillDepth = PropertiesUtil.getValue(properties, "estimate.fill-depth", fillDepth);
        goldModeTime = PropertiesUtil.getValue(properties, "estimate.onegold-time", goldModeTime);
        goldmodeCounterForced = PropertiesUtil.getValue(properties, "estimate.onegold-forced-time", goldmodeCounterForced);

        window = PropertiesUtil.getValue(properties, "estimate.window-size", window);
        closeGoldLimit = PropertiesUtil.getValue(properties, "estimate.window-gold-limit", closeGoldLimit);
    }

    private static WeightPolicy POLICY = new LinearWeightPolicy(GOLD_COST, RATE_INFLATION);

    private static  FillerResultHolder BEST_SINGLE = null;

    public static class Result {
        DoubleMask mask;
        Optional<FillerResultHolder> bestGold = Optional.empty();

        public DoubleMask getMask() {
            return mask;
        }

        private void setMask(DoubleMask mask) {
            this.mask = mask;
        }

        public Optional<FillerResultHolder> getBestGold() {
            return bestGold;
        }

        private void bestGold(FillerResultHolder gold) {
            this.bestGold = Optional.of(gold);
        }

        public GameCommand bestCommand() {
            return bestGold.map( way -> way.getHeroState())
                    .map( state -> state.getCommand().toGameCommand())
                    .orElse(DoNothingCommand.DO_NOTHING);
        }

        public Integer bestGoldDistance() {
            return bestGold.map( way -> way.getHeroState())
                    .map(state -> state.getGeneration())
                    .orElse(null);
        }
    }
}
