package org.silentpom.runner.algo.solve.commands;

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
    public Position moveOnly(Position x) {
        return x.left();
    }
}
