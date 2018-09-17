package org.silentpom.runner.domain.state;

import org.silentpom.runner.domain.Position;

/**
 * Created by Vlad on 12.09.2018.
 */
public class PositionsCache {
    private int N = 108;
    private int SHIFT = 20;
    private Position[][] cache = new Position[N][N];

    public Position position(int row, int column) {
        Position pos = cache[SHIFT + row][SHIFT + column];
        if (pos != null) {
            return pos;
        }
        pos = new Position(row, column);
        cache[SHIFT + row][SHIFT + column] = pos;
        return pos;
    }

    public static Position make(int row, int column) {
        return ONE.position(row, column);
    }


    private static PositionsCache ONE = new PositionsCache();
}
