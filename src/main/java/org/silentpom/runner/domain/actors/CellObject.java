package org.silentpom.runner.domain.actors;

import org.silentpom.runner.domain.CellType;
import org.silentpom.runner.domain.Position;

/**
 * Created by Vlad on 12.09.2018.
 */
public abstract class CellObject {
    Position position;

    protected CellObject(Position position) {
        this.position = position;
    }

    protected Position startPosition() {
        return position;
    }

    public Position position(int time) {
        return position;
    }

    public abstract CellType getCellType(int time, Position pos, boolean hide);
}
