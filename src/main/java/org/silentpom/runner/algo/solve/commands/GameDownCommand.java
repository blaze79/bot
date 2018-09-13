package org.silentpom.runner.algo.solve.commands;

import org.silentpom.runner.domain.CellCategory;
import org.silentpom.runner.domain.CellType;
import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.maps.CommonMap;

/**
 * Created by Vlad on 09.09.2018.
 */
public class GameDownCommand extends BaseGameCommand {

    @Override
    public String getCode() {
        return "DOWN";
    }

    @Override
    public Position moveOnly(Position x) {
        return x.down();
    }

    @Override
    protected boolean hasConditionToStartMove(CellType cellType, CellType newType, boolean canKill) {
        if (cellType == CellType.LADDER || cellType == CellType.PIPE) {
            return true;
        }

        if (newType == CellType.LADDER) {
            return true;
        }

        if (canKill) {
            if (newType.getCategory() == CellCategory.BOT || newType.getCategory() == CellCategory.MY) {
                return true;
            }
        }

        return false;
    }
}
