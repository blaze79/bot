package org.silentpom.runner.domain;

import org.silentpom.runner.domain.state.PositionsCache;

/**
 * Created by Vlad on 09.09.2018.
 */
public class Position {
    int row;
    int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Position left() {
        return PositionsCache.make(row, column-1);
    }

    public Position right() {
        return PositionsCache.make(row, column+1);
    }

    public Position up() {
        return PositionsCache.make(row - 1, column);
    }

    public Position down() {
        return PositionsCache.make(row + 1, column);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (row != position.row) return false;
        return column == position.column;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 4095 * result + column;
        return result;
    }

}
