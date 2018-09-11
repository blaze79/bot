package org.silentpom.runner.domain;

/**
 * Created by Vlad on 09.09.2018.
 */
public enum CellCategory {
    FREE,
    WALL,
    HOLE,
    STAIRS,
    MY,
    BOT,
    ENEMY,
    GOLD;

    public static boolean isFreeCell(CellCategory category) {
        return category == FREE || category == STAIRS || category == GOLD;
    }
}
