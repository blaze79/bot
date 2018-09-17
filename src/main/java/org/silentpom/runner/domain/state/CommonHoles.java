package org.silentpom.runner.domain.state;

import org.silentpom.runner.domain.CellType;
import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.actors.HoleCell;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vlad on 12.09.2018.
 */
public class CommonHoles {
    List<HoleCell> holes;
    int ticks = 0;

    public CommonHoles(List<HoleCell> holes) {
        this.holes = holes;
    }

    public void startNewTick() {
        ticks++;
    }

    public void tickBack() {
        ticks--;
    }

    public CellType cellType(Position pos) {

        for (HoleCell hole : holes) {
            if (pos.equals(hole.position(0))) {
                CellType cellType = hole.getCellType(ticks, pos, false);
                if (cellType != null) {
                    return cellType;
                }
            }
        }
        return null;
    }
}
