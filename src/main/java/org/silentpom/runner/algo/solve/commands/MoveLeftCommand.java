package org.silentpom.runner.algo.solve.commands;

import org.silentpom.runner.domain.CellType;
import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.maps.CommonMap;

/**
 * Created by Vlad on 09.09.2018.
 */
public class MoveLeftCommand implements GameCommand {

    @Override
    public String getCode() {
        return "LEFT";
    }

    @Override
    public Position moveOnly(Position x) {
        return x.left();
    }

    @Override
    public boolean moveInGame(CommonMap map, CommandResult result, boolean canKill) {
        Position side = moveOnly(result.getPosition());
        CellType cell = map.getCell(side);
        if(cell.isFreeCell()) {
            result.setPosition(side);
            return true;
        }

        if(result.tryToKill(cell, side, canKill)) {
            result.setPosition(side);
            return true;
        }

        return false;
    }
}
