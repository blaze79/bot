package org.silentpom.runner.algo.solve.commands;

import org.silentpom.runner.domain.CellCategory;
import org.silentpom.runner.domain.CellType;
import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.maps.CommonMap;

/**
 * Created by Vlad on 09.09.2018.
 * <p>
 * dig left
 * move left
 * down to the hole
 * down to destination
 */
public class DigLeftCommand implements GameCommand {

    @Override
    public String getCode() {
        return "DIG LEFT";
    }

    @Override
    public Position moveOnly(Position x) {
        return x;
    }

    @Override
    public boolean moveInGame(CommonMap map, CommandResult result, boolean canKill) {
        Position position = result.getPosition();
        Position side = position.left();

        CellType sideCell = map.getCell(side);
        if (sideCell == CellType.NONE) {
            Position down = side.down();
            CellType downCell = map.getCell(down);
            if (downCell == CellType.BRICK) {
                result.setHole(down);
                return true;
            }
        }

        return false;
    }
}
