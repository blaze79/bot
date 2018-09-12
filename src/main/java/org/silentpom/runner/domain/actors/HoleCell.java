package org.silentpom.runner.domain.actors;

import org.silentpom.runner.domain.CellType;
import org.silentpom.runner.domain.Position;

/**
 * Created by Vlad on 12.09.2018.
 */
public class HoleCell extends CellObject {
    int tick;

    public HoleCell(Position position, CellType type) {
        super(position);
        this.tick = findBirthTick(type);
    }

    @Override
    public CellType getCellType(int time) {
        return findCellType(time, tick);
    }

    private int findBirthTick(CellType type) {
        switch (type) {
            case DRILL_PIT:
                return 0;
            case PIT_FILL_1:
                return -1;
            case PIT_FILL_2:
                return -2;
            case PIT_FILL_3:
                return -3;
            case PIT_FILL_4:
                return -4;
        }

        return -10;
    }

    public static CellType findCellType(int time, int tick) {
        if (time < tick) {
            return null;
        }
        switch (time - tick) {
            case 0:
                return CellType.DRILL_PIT;
            case 1:
                return CellType.PIT_FILL_1;
            case 2:
                return CellType.PIT_FILL_2;
            case 3:
                return CellType.PIT_FILL_3;
            case 4:
                return CellType.PIT_FILL_4;

        }
        return null;
    }

}
