package org.silentpom.runner.domain.masks;

import org.silentpom.runner.domain.CellType;
import org.silentpom.runner.domain.Position;

/**
 * Created by Vlad on 09.09.2018.
 */
public class BitMask {
    int rows;
    int columns;
    boolean checked[][];

    public BitMask(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        checked = new boolean[rows][columns];
        clear();
    }

    private void clear() {
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                checked[i][j] = false;
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public boolean getChecked(int row, int column) {
        return checked[row][column];
    }

    public boolean getChecked(Position pos) {
        return getChecked(pos.getRow(), pos.getColumn());
    }

    public void setChecked(int row, int column, boolean data) {
        checked[row][column] = data;
    }

    public void setChecked(Position pos, boolean data) {
         setChecked(pos.getRow(), pos.getColumn(), data);
    }

    public void print() {
        System.out.println();
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                System.out.print(checked[i][j]? 1 : 0);
            }
            System.out.println();
        }
        System.out.println();
    }
}
