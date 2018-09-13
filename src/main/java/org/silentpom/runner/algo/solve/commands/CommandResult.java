package org.silentpom.runner.algo.solve.commands;

import org.silentpom.runner.domain.CellCategory;
import org.silentpom.runner.domain.CellType;
import org.silentpom.runner.domain.Position;

/**
 * Created by Vlad on 13.09.2018.
 */
public class CommandResult {
    Position position;
    Position kill;
    Position hole;

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getKill() {
        return kill;
    }

    public void setKill(Position kill) {
        this.kill = kill;
    }

    public Position getHole() {
        return hole;
    }

    public void setHole(Position hole) {
        this.hole = hole;
    }

    public void resetPosition(Position pos) {
        position = pos;
        hole = null;
        kill = null;
    }

    public boolean tryToKill(CellType object, Position kill, boolean canKill) {
        if(canKill) {
            if(object.getCategory() == CellCategory.BOT || object.getCategory() == CellCategory.MY) {
                setKill(kill);
                return true;
            }
        }
        return false;
    }
}
