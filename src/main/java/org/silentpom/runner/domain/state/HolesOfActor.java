package org.silentpom.runner.domain.state;

import org.silentpom.runner.domain.CellType;
import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.actors.HoleCell;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vlad on 12.09.2018.
 */
public class HolesOfActor {
    List<Position> timeStack = new ArrayList<>();

    public HolesOfActor() {
    }

    public void startNewTick(Position hole) {
        timeStack.add(hole);
    }

    public void changeLastHole(Position hole) {
        timeStack.set(timeStack.size() - 1, hole);
    }

    public void tickBack() {
        timeStack.remove(timeStack.size() - 1);
    }

    public CellType cellType(Position pos) {
        for (int i = timeStack.size() - 1; i >= 0; --i) {
            Position hole = timeStack.get(i);
            if (pos.equals(hole)) {
                CellType cellType = HoleCell.findCellType(timeStack.size() - 1, i);
                if (cellType != null) {
                    return cellType;
                }
            }
        }

        return null;
    }
}
