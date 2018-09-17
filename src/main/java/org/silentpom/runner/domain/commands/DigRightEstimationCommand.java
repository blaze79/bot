package org.silentpom.runner.domain.commands;

import org.silentpom.runner.algo.solve.commands.DigRightCommand;
import org.silentpom.runner.algo.solve.commands.GameCommand;
import org.silentpom.runner.algo.solve.commands.GameRightCommand;
import org.silentpom.runner.domain.CellCategory;
import org.silentpom.runner.domain.CellType;
import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.maps.CommonMap;

/**
 * Created by Vlad on 09.09.2018.
 *
 * dig left
 * move left
 * down to the hole
 * down to destination
 *
 */
public class DigRightEstimationCommand implements MoveCommand {
    @Override
    public Position moveCommand(Position x, CommonMap map) {
        Position right = x.right();
        if (!map.getCell(x.down()).canStayOn()) {
            return null;
        }

        if (map.getCell(right) == CellType.NONE) {
            Position rightDown = right.down();
            if(map.getCell(rightDown) == CellType.BRICK || map.getCell(rightDown).getCategory() == CellCategory.HOLE) {
                return rightDown.down();
            }
        }

        return null;
    }

    @Override
    public String getCode() {
        return "DIG RIGHT(.. RIGHT DOWN DOWN)";
    }

    @Override
    public int tickCount() {
        return 4;
    }

    @Override
    public GameCommand toGameCommand() {
        GameCommand gameCommand = COMMAND[COUNTER++];
        COUNTER = COUNTER%2;
        return gameCommand;
    }

    public static GameCommand COMMAND[]={ new DigRightCommand(), new GameRightCommand()};
    private static int COUNTER = 0;

}
