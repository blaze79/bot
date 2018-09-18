package org.silentpom.runner.algo.solve;

import org.silentpom.runner.algo.estimation.policy.LinearWeightPolicy;
import org.silentpom.runner.algo.solve.commands.*;
import org.silentpom.runner.domain.CellCategory;
import org.silentpom.runner.domain.CellType;
import org.silentpom.runner.domain.Constants;
import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.actors.Hero;
import org.silentpom.runner.domain.actors.Hunter;
import org.silentpom.runner.domain.maps.CommonMap;
import org.silentpom.runner.domain.maps.FullMapInfo;
import org.silentpom.runner.domain.masks.DoubleMask;
import org.silentpom.runner.domain.state.FullMapAtTime;

import java.util.List;

import static org.silentpom.runner.domain.Constants.RATE_INFLATION;

/**
 * Created by Vlad on 18.09.2018.
 */
public class OnlyHeroSolver implements ProblemSolver {

    CommandResult tempResult = new CommandResult();
    int maxDepth = 5;
    double[] resTemp;

    public OnlyHeroSolver() {
        resTemp = new double[maxDepth + 10];
        clearRes();
    }

    @Override
    public GameCommand findBestCommand(DoubleMask estimate, FullMapInfo info) {
        FullMapAtTime map = new FullMapAtTime(info);
        clearRes();
        return findGameCommandRec(estimate, map, 0);
    }

    private void clearRes() {
        for (int i = 0; i < resTemp.length; ++i) {
            resTemp[i] = 0;
        }
    }

    private GameCommand findGameCommandRec(DoubleMask estimate, FullMapAtTime mapAtTime, int level) {
        Hero hero = mapAtTime.getHero();
        if (level == maxDepth) {
            resTemp[level] = estimate.getChecked(hero.position(level));
            return null;
        }

        mapAtTime.newTick();
        CommonMap heroView = mapAtTime.getHeroView();
        GameCommand lastCommand = hero.getLastCommand();
        try {
            if (lastCommand == null) {
                double maxValue = -1e8;
                GameCommand maxCommand = null;

                for (GameCommand command : HERO_COMMANDS) {
                    tempResult.resetPosition(hero.position(level));

                    boolean canBeMoved = command.moveInGame(heroView, tempResult, false);
                    if (canBeMoved) {
                        Position newPosition = tempResult.getPosition();
                        mapAtTime.processHeroMove(newPosition, tempResult);

                        if (checkKilled(newPosition, mapAtTime, heroView)) {
                            continue;
                        }
                        double currentValue = estimatePosition(level, newPosition, mapAtTime, heroView);
                        tryNextLevel(estimate, mapAtTime, heroView, level);
                        currentValue += GAME_POLICY.reduceWeight(resTemp[level + 1], 1);

                        if (currentValue > maxValue) {
                            maxValue = currentValue;
                            maxCommand = command;
                        }
                    }
                }

                resTemp[level] = maxValue;
                return maxCommand != null ? maxCommand : DEAD_COMMAND;
            } else {
                tempResult.resetPosition(hero.position(level));
                resTemp[level] = -1e8;

                lastCommand.moveInGame(heroView, tempResult, false);
                Position newPosition = tempResult.getPosition();
                mapAtTime.processHeroMove(newPosition, tempResult);

                if (checkKilled(newPosition, mapAtTime, heroView)) {
                    return DEAD_COMMAND;
                }
                double currentValue = estimatePosition(level, newPosition, mapAtTime, heroView);
                tryNextLevel(estimate, mapAtTime, heroView, level);
                currentValue += GAME_POLICY.reduceWeight(resTemp[level + 1], 1);

                resTemp[level] = currentValue;
                return lastCommand;
            }
        } finally {
            mapAtTime.tickBack();
        }
    }

    boolean checkKilled(Position newPosition, FullMapAtTime mapAtTime, CommonMap heroView) {
        if (heroView.getCell(newPosition.left()).getCategory() == CellCategory.WALL &&
                heroView.getCell(newPosition.right()).getCategory() == CellCategory.WALL &&
                !heroView.getCell(newPosition.right()).isFreeCell()
                ) {
            return true;
        }

        List<Hunter> hunters = mapAtTime.getHunters();
        for (int h = 0; h < hunters.size(); ++h) {
            Hunter hunter = hunters.get(h);
            Position hunterPosition = hunter.position(0);
            if (newPosition.absDistance(hunterPosition) == 1) {
                if (hunterPosition.getRow() == newPosition.getRow()) {
                    if (heroView.getCell(hunterPosition.down()).canStayOn()) {
                        return true;
                    }
                } else {
                    if (hunterPosition.getRow() < newPosition.getRow()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private double estimatePosition(int level, Position newPosition, FullMapAtTime mapAtTime, CommonMap heroView) {
        double value = 0;
        if (heroView.getCell(newPosition) == CellType.GOLD) {
            value += GAME_POLICY.startWeight(newPosition);
        }
        List<Hunter> hunters = mapAtTime.getHunters();
        for (int h = 0; h < hunters.size(); ++h) {
            Hunter hunter = hunters.get(h);
            Position hunterPosition = hunter.position(level);
            if (newPosition.absDistance(hunterPosition) == 1) {
                value -= Constants.NEAR_HUNTER;
            }
            if (hunterPosition.getRow() == newPosition.getRow()) {
                int min = Math.min(hunterPosition.getColumn(), newPosition.getColumn());
                int max = Math.max(hunterPosition.getColumn(), newPosition.getColumn());
                for (int i = min; i < max; i++) {
                    if (heroView.getCell(hunterPosition.getRow(), i).getCategory() == CellCategory.HOLE) {
                        value += Constants.NEAR_HUNTER;
                        break;
                    }
                }
            }
        }
        return value;
    }

    private void tryNextLevel(DoubleMask estimate, FullMapAtTime mapAtTime, CommonMap heroView, int level) {
        findGameCommandRec(estimate, mapAtTime, level + 1);
    }


    public static GameCommand[] HERO_COMMANDS = {
            new GameLeftCommand(), new GameUpCommand(), new GameRightCommand(), new GameDownCommand(),
            new DigLeftCommand(), new DigRightCommand(),
            new DoNothingCommand()
    };

    public static LinearWeightPolicy GAME_POLICY = new LinearWeightPolicy(Constants.REAL_GOLD_COST, RATE_INFLATION);

    public static GameCommand DEAD_COMMAND = new DieCommand();
}
