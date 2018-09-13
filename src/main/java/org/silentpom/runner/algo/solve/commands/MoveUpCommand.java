package org.silentpom.runner.algo.solve.commands;

import org.silentpom.runner.domain.CellType;
import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.maps.CommonMap;

/**
 * Created by Vlad on 09.09.2018.
 */
public class MoveUpCommand implements GameCommand {

    @Override
    public String getCode() {
        return "UP";
    }

    @Override
    public Position moveOnly(Position x) {
        return x.up();
    }

    @Override
    public boolean moveInGame(CommonMap map, CommandResult result, boolean canKill) {
        Position position = result.getPosition();
        Position up = moveOnly(position);
        CellType cell = map.getCell(position);
        CellType cellUp = map.getCell(up);

        if (cell == CellType.LADDER) {
            if (cellUp.isFreeCell()) {
                result.setPosition(up);
                return true;
            }

            if (result.tryToKill(cellUp, up, canKill)) {
                result.setPosition(up);
                return true;
            }
            return false;
        }
        return false;
    }
}
