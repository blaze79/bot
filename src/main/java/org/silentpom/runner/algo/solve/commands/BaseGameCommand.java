package org.silentpom.runner.algo.solve.commands;

import org.silentpom.runner.domain.CellType;
import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.maps.CommonMap;

/**
 * Created by Vlad on 14.09.2018.
 */
public abstract class BaseGameCommand implements GameCommand {

    @Override
    public boolean moveInGame(CommonMap map, CommandResult result, boolean canKill) {
        Position current = result.getPosition();
        Position newPosition = this.moveOnly(current);

        CellType cellType = map.getCell(current);
        CellType newType = map.getCell(newPosition);

        if(hasConditionToStartMove(cellType, newType, canKill)) {
            if(newType.isFreeCell()) {
                result.setPosition(newPosition);
                return true;
            }

            if(result.tryToKill(newType, newPosition, canKill)) {
                result.setPosition(newPosition);
                return true;
            }
        }

        return false;
    }

    protected abstract boolean hasConditionToStartMove(CellType cellType, CellType newType, boolean canKill);

}
