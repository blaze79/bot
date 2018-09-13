package org.silentpom.runner.algo.solve.commands;

import org.silentpom.runner.domain.CellType;
import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.maps.CommonMap;

/**
 * Created by Vlad on 09.09.2018.
 */
public class MoveDownCommand implements GameCommand {

    @Override
    public String getCode() {
        return "DOWN";
    }

    @Override
    public Position moveOnly(Position x) {
        return x.down();
    }

    @Override
    public boolean moveInGame(CommonMap map, CommandResult result, boolean canKill) {
        Position position = result.getPosition();
        Position down = moveOnly(position);
        CellType cell = map.getCell(position);
        CellType cellDown = map.getCell(down);

        if (cell == CellType.LADDER || cell == CellType.PIPE) {
            if (cellDown.isFreeCell()) {
                result.setPosition(down);
                return true;
            }

            if (result.tryToKill(cellDown, down, canKill)) {
                result.setPosition(down);
                return true;
            }
            return false;
        }
        if (cellDown == CellType.LADDER) {
            result.setPosition(down);
            return true;
        }

        if (result.tryToKill(cellDown, down, canKill)) {
            result.setPosition(down);
            return true;
        }
        return false;
    }
}
