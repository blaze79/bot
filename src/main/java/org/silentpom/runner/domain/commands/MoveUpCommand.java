package org.silentpom.runner.domain.commands;

import org.silentpom.runner.domain.CellType;
import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.maps.CommonMap;

/**
 * Created by Vlad on 09.09.2018.
 */
public class MoveUpCommand implements MoveCommand {
    @Override
    public Position moveCommand(Position x, CommonMap map) {
        if (map.getCell(x) == CellType.LADDER) {
            return x.up();
        }

        return null;
    }

    @Override
    public String getCode() {
        return "UP";
    }

}
