package org.silentpom.runner.domain.commands;

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
public class DigLeftEstimationCommand implements MoveCommand {
    @Override
    public Position moveCommand(Position x, CommonMap map) {
        Position left = x.left();
        if (map.getCell(left) == CellType.NONE) {
            Position leftDown = left.down();
            if(map.getCell(leftDown) == CellType.BRICK || map.getCell(leftDown).getCategory() == CellCategory.HOLE) {
                return leftDown.down();
            }
        }

        return null;
    }

    @Override
    public String getCode() {
        return "DIG LEFT(.. LEFT DOWN DOWN)";
    }

    @Override
    public int tickCount() {
        return 4;
    }

    @Override
    public Position moveOnly(Position x) {
        return x;
    }
}
