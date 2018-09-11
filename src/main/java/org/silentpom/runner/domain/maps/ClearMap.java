package org.silentpom.runner.domain.maps;

import org.silentpom.runner.domain.CellCategory;
import org.silentpom.runner.domain.CellInfo;
import org.silentpom.runner.domain.CellType;

import java.util.ArrayList;
import java.util.List;

import static org.silentpom.runner.domain.CellType.*;

/**
 * Created by Vlad on 09.09.2018.
 */
public class ClearMap implements CommonMap {
    int rows;
    int columns;
    CellType[][] cells;

    private ClearMap(int rows, int columns, CellType[][] cells) {
        this.rows = rows;
        this.columns = columns;
        this.cells = cells;
    }

    @Override
    public int rows() {
        return rows;
    }

    @Override
    public int columns() {
        return columns;
    }

    @Override
    public CellType getCell(int i, int j) {
        return cells[i][j];
    }

    @Override
    public List<CellInfo> selectCells(CellFilter filter) {
        ArrayList<CellInfo> list = new ArrayList<CellInfo>();
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                if (filter.match(cells[i][j])) {
                    list.add(new CellInfo(i, j, cells[i][j]));
                }
            }
        }
        return list;
    }

    private void init() {
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                cells[i][j] = morf(cells[i][j]);
            }
        }
    }

    private CellType morf(CellType type) {
        switch (type.getCategory()) {
            case FREE:
                return type;
            case STAIRS:
                return type;
            case WALL:
                return type;
            case GOLD:
                return type;
            case HOLE:
                return CellType.BRICK;
            case MY:
                return fixMy(type);
            case BOT:
                return fixBot(type);
            case ENEMY:
                return fixEnemy(type);
        }

        return type;
    }

    private CellType fixEnemy(CellType type) {
        switch (type) {
            case ENEMY_LADDER: return CellType.LADDER;
            case ENEMY_LEFT:  return CellType.NONE;
            case ENEMY_RIGHT: return CellType.NONE;
            case ENEMY_PIPE_LEFT: return CellType.PIPE;
            case ENEMY_PIPE_RIGHT:return CellType.PIPE;
            case ENEMY_PIT: return CellType.BRICK;

        }
        return CellType.NONE;
    }

    private CellType fixBot(CellType type) {
        switch (type) {
            case OTHER_HERO_DIE:
                return CellType.NONE;
            case OTHER_HERO_LEFT:
                return CellType.NONE;
            case OTHER_HERO_RIGHT:
                return CellType.NONE;
            case OTHER_HERO_LADDER:
                return CellType.LADDER;
            case OTHER_HERO_PIPE_LEFT:
                return CellType.PIPE;
            case OTHER_HERO_PIPE_RIGHT:
                return CellType.PIPE;
        }
        return CellType.NONE;
    }

    private CellType fixMy(CellType type) {
        switch (type) {
            case HERO_DIE:
                return CellType.NONE;
            case HERO_DRILL_LEFT:
                return CellType.NONE;
            case HERO_DRILL_RIGHT:
                return CellType.NONE;
            case HERO_LADDER:
                return CellType.LADDER;
            case HERO_LEFT:
                return CellType.NONE;
            case HERO_RIGHT:
                return CellType.NONE;
            case HERO_FALL_LEFT:
                return CellType.NONE;
            case HERO_FALL_RIGHT:
                return CellType.NONE;
            case HERO_PIPE_LEFT:
                return CellType.PIPE;
            case HERO_PIPE_RIGHT:
                return CellType.PIPE;
        }

        return CellType.NONE;
    }


    public void print() {
        System.out.println();
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                System.out.print(cells[i][j].getCode());
            }
            System.out.println();
        }
        System.out.println();
    }

    public void checkCells() {
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                switch (cells[i][j].getCategory()) {
                    case FREE:
                    case WALL:
                    case STAIRS:
                    case GOLD:
                        continue;
                    default:
                        throw new RuntimeException("Incorrect map category: " + cells[i][j].getCategory());
                }
            }
        }
    }

    public CellType[][] copyMap() {
        CellType[][] raw = new CellType[rows][columns];
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                raw[i][j] = cells[i][j];
            }
        }
        return raw;
    }

    public static ClearMap fromMap(SimpleMap simpleMap) {
        ClearMap map = new ClearMap(simpleMap.rows(), simpleMap.columns(), simpleMap.copyMap());
        map.init();
        return map;
    }

}
