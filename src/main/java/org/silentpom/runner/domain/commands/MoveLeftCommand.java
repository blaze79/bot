package org.silentpom.runner.domain.commands;

import org.silentpom.runner.algo.solve.commands.GameCommand;
import org.silentpom.runner.algo.solve.commands.GameLeftCommand;
import org.silentpom.runner.domain.CellCategory;
import org.silentpom.runner.domain.CellType;
import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.maps.CommonMap;

/**
 * Created by Vlad on 09.09.2018.
 */
public class MoveLeftCommand implements MoveCommand {

    @Override
    public Position moveCommand(Position x, CommonMap map) {
        if (map.getCell(x) == CellType.LADDER || map.getCell(x) == CellType.PIPE) {
            return x.left();
        }

        if (map.getCell(x.down()).canStayOn()) {
            return x.left();
        }

        return null;
    }

    @Override
    public String getCode() {
        return "LEFT";
    }

    @Override
    public GameCommand toGameCommand() {
        return new GameLeftCommand();
    }
}
