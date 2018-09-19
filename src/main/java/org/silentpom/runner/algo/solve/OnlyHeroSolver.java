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
import org.silentpom.runner.domain.state.PositionsCache;

import java.util.Arrays;
import java.util.List;

import static org.silentpom.runner.domain.Constants.RATE_INFLATION;

/**
 * Created by Vlad on 18.09.2018.
 */
public class OnlyHeroSolver implements ProblemSolver {

    CommandResult tempResult = new CommandResult();
    int maxDepth = 5;
    double[] resTemp;
    int calls = 0;

    public OnlyHeroSolver(int depth) {
        maxDepth = depth;
        resTemp = new double[maxDepth + 10];
        clearRes();
    }

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
        calls++;

//        char[] chars = new char[level];
//        Arrays.fill(chars, '\t');
//        String tabs = new String(chars);


        mapAtTime.newTick();
        CommonMap heroView = mapAtTime.getHeroView();
        GameCommand lastCommand = hero.getLastCommand();
        try {
            if (lastCommand == null) {
                double maxValue = -1e8;
                GameCommand maxCommand = null;

                for (GameCommand command : HERO_COMMANDS) {
                    hero.changePosition(null);
                    hero.changeHole(null);

                    tempResult.resetPosition(hero.position(level));

                    boolean canBeMoved = command.moveInGame(heroView, tempResult, false);
                    if (canBeMoved) {
                        Position newPosition = tempResult.getPosition();
                        CellType oldCellType = mapAtTime.processHeroMove(newPosition, tempResult);

                        if (checkKilled(newPosition, mapAtTime, heroView)) {
//                            System.out.printf("%s (%d) Command %s was killed, skipped %n", tabs, level, command.getCode());
                            continue;
                        }
                        double currentValue = estimatePosition(level, newPosition, mapAtTime, heroView, oldCellType, tempResult.getHole());
//                        System.out.printf("%s (%d) Command %s local weight %f. Position: %d %d %n", tabs, level, command.getCode(), currentValue,
//                                newPosition.getRow(), newPosition.getColumn()
//                        );

                        tryNextLevel(estimate, mapAtTime, heroView, level);
                        currentValue += GAME_POLICY.reduceWeight(resTemp[level + 1], 1);

                        if (currentValue > maxValue) {
                            maxValue = currentValue;
                            maxCommand = command;
                        }
                    }
                }

                resTemp[level] = maxValue;
                GameCommand retCommand = (maxCommand != null ? maxCommand : DEAD_COMMAND);
//                System.out.printf("%s (%d) MAX Command  IS %s local weight %f %n", tabs, level, retCommand.getCode(), maxValue);
                return retCommand;
            } else {
                tempResult.resetPosition(hero.position(level));
                resTemp[level] = -1e8;

                lastCommand.moveInGame(heroView, tempResult, false);
//                System.out.printf("%s (%d) Command NO CHANCE %s was used: %b %n", tabs, level, lastCommand.getCode(), true);
                Position newPosition = tempResult.getPosition();
                CellType oldCellType = mapAtTime.processHeroMove(newPosition, tempResult);

                if (checkKilled(newPosition, mapAtTime, heroView)) {
                    return DEAD_COMMAND;
                }
                double currentValue = estimatePosition(level, newPosition, mapAtTime, heroView, oldCellType, tempResult.getHole());
//                System.out.printf("%s (%d) Command NO CHANCE %s local weight %f %n", tabs, level, lastCommand.getCode(), currentValue);

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
                !heroView.getCell(newPosition.down()).isFreeCell()
                ) {
            return true;
        }

        List<Hunter> hunters = mapAtTime.getHunters();
        for (int h = 0; h < hunters.size(); ++h) {
            Hunter hunter = hunters.get(h);
            Position hunterPosition = hunter.position(0);
            if (newPosition.absDistance(hunterPosition) == 1) {
                if (hunterPosition.getRow() == newPosition.getRow()) {
                    if (heroView.getCell(hunterPosition.down()).canStayOn() ||
                            mapAtTime.getClearMap().getCell(hunterPosition).getCategory() == CellCategory.STAIRS) {
                        return true;
                    }
                } else {
                    if (hunterPosition.getRow() < newPosition.getRow()) {
                        return true;
                    } else if(mapAtTime.getClearMap().getCell(hunterPosition) ==  CellType.LADDER) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private double estimatePosition(int level, Position newPosition, FullMapAtTime mapAtTime, CommonMap heroView, CellType oldCellType, Position newHole) {
        double value = 0;
        if (oldCellType == CellType.GOLD) {
            value += GAME_POLICY.startWeight(newPosition);
        }
        List<Hunter> hunters = mapAtTime.getHunters();
        for (int h = 0; h < hunters.size(); ++h) {
            Hunter hunter = hunters.get(h);
            Position hunterPosition = hunter.position(level);
            if (heroView.getCell(hunterPosition.down()).getCategory() == CellCategory.HOLE) {
                continue;
            }

            if (hunterPosition.getRow() == newPosition.getRow() && newPosition.absDistance(hunterPosition) < 4) {
                int min = Math.min(hunterPosition.getColumn(), newPosition.getColumn());
                int max = Math.max(hunterPosition.getColumn(), newPosition.getColumn());

                int oldHoles = 0, allHoles = 0;
                for (int i = min; i < max; i++) {
                    Position down = PositionsCache.make(hunterPosition.getRow() + 1, i);
                    if (heroView.getCell(down).getCategory() == CellCategory.HOLE) {
                        allHoles++;
                        if (!down.equals(newHole)) {
                            oldHoles++;
                        }
                    }
                }


                if (oldHoles == 0 && allHoles > 0) {
                    value += Constants.NEAR_HOLE_HUNTER;
                } else {
                    if (oldHoles == 0) {
                        value += Constants.NEAR_HUNTER * (4 - newPosition.absDistance(hunterPosition)) / 4;
                    }
                }
            } else if(newPosition.absDistance(hunterPosition) < 4){
                value += Constants.NEAR_HUNTER * (4 - newPosition.absDistance(hunterPosition)) / 4;
            }
        }
        return value;
    }

    private GameCommand tryNextLevel(DoubleMask estimate, FullMapAtTime mapAtTime, CommonMap heroView, int level) {
        return findGameCommandRec(estimate, mapAtTime, level + 1);
    }

    public int getCalls() {
        return calls;
    }

    public double getValue() {
        return resTemp[0];
    }

    /*public static GameCommand[] HERO_COMMANDS = {
            new GameLeftCommand(), new GameUpCommand(), new GameRightCommand(), new GameDownCommand(),
            new DigLeftCommand(), new DigRightCommand(),
            new DoNothingCommand()
    };*/

    public static GameCommand[] HERO_COMMANDS = {
            new GameRightCommand(), new GameLeftCommand(), new GameUpCommand(), new GameDownCommand(),
            new DigLeftCommand(), new DigRightCommand(),
            new DoNothingCommand()
    };

    public static LinearWeightPolicy GAME_POLICY = new LinearWeightPolicy(Constants.REAL_GOLD_COST, RATE_INFLATION);

    public static GameCommand DEAD_COMMAND = new DieCommand();
}
