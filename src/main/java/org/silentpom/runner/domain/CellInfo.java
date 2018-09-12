package org.silentpom.runner.domain;

import org.silentpom.runner.domain.state.PositionsCache;

/**
 * Created by Vlad on 09.09.2018.
 */
public class CellInfo {
    Position position;
    CellType type;

    public CellInfo(int row, int column, CellType type) {
        this.type = type;
        this.position = PositionsCache.make(row, column);
    }

    public CellInfo(Position position, CellType type) {
        this.position = position;
        this.type = type;
    }

    public Position getPosition() {
        return position;
    }

    public CellType getType() {
        return type;
    }
}
