package org.silentpom.runner.algo.solve.commands;

import org.silentpom.runner.domain.CellType;
import org.silentpom.runner.domain.Position;

/**
 * Created by Vlad on 09.09.2018.
 */
public class GameRightCommand extends BaseGameCommand {

    @Override
    public String getCode() {
        return "RIGHT";
    }

    @Override
    public Position moveOnly(Position x) {
        return x.right();
    }

    @Override
    protected boolean hasConditionToStartMove(CellType cellType, CellType newType, boolean canKill) {
        return true;
    }
}
