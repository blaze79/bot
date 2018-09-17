package org.silentpom.runner.algo.solve;

import org.silentpom.runner.algo.solve.commands.*;
import org.silentpom.runner.domain.CellType;
import org.silentpom.runner.domain.Constants;
import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.actors.Hero;
import org.silentpom.runner.domain.maps.CommonMap;
import org.silentpom.runner.domain.maps.FullMapInfo;
import org.silentpom.runner.domain.masks.DoubleMask;
import org.silentpom.runner.domain.state.FullMapAtTime;

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

        CommonMap heroView = mapAtTime.getHeroView();
        GameCommand lastCommand = hero.getLastCommand();

        if (lastCommand == null) {
            double maxValue = -1e8;
            GameCommand maxCommand = null;

            for (GameCommand command : HERO_COMMANDS) {
                tempResult.resetPosition(hero.position(level));
                resTemp[level] = 0;

                boolean canBeMoved = command.moveInGame(heroView, tempResult, false);
                if (canBeMoved) {
                    Position newPosition = tempResult.getPosition();
                    tryNextLevel(estimate, mapAtTime, heroView, newPosition, level);

                    if (resTemp[level] > maxValue) {
                        maxValue = resTemp[level];
                        maxCommand = command;
                    }
                }

                return maxCommand;
            }
        } else {
            tempResult.resetPosition(hero.position(level));
            resTemp[level] = 0;

            lastCommand.moveInGame(heroView, tempResult, false);
            Position newPosition = tempResult.getPosition();

            tryNextLevel(estimate, mapAtTime, heroView, newPosition, level);

//            if (heroView.getCell(newPosition) == CellType.GOLD) {
//                resTemp[level] = takeGold(level);
//            }
//            mapAtTime.newTick();
//            mapAtTime.moveHero(newPosition);
//
//            findGameCommandRec(estimate, mapAtTime, level + 1);
//            resTemp[level] += resTemp[level + 1];
//            mapAtTime.tickBack();

            return lastCommand;
        }

        return null;
    }

    private void tryNextLevel(DoubleMask estimate, FullMapAtTime mapAtTime, CommonMap heroView, Position newPosition, int level) {
        if (heroView.getCell(newPosition) == CellType.GOLD) {
            resTemp[level] = takeGold(level);
        }

        mapAtTime.newTick();
        mapAtTime.moveHero(newPosition);

        findGameCommandRec(estimate, mapAtTime, level + 1);
        resTemp[level] += resTemp[level + 1];
        mapAtTime.tickBack();
    }

    double takeGold(int level) {
        return Constants.REAL_GOLD_COST * Math.pow(Constants.RATE_INFLATION, level);
    }

    public static GameCommand[] HERO_COMMANDS = {
            new GameLeftCommand(), new GameUpCommand(), new GameRightCommand(), new GameDownCommand(),
            new DigLeftCommand(), new DigRightCommand(),
            new DoNothingCommand()
    };
}
