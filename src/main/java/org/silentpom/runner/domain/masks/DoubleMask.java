package org.silentpom.runner.domain.masks;

import org.silentpom.runner.domain.Position;

/**
 * Created by Vlad on 09.09.2018.
 */
public class DoubleMask {
    int rows;
    int columns;
    double values[][];

    public DoubleMask(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        values = new double[rows][columns];
        clear();
    }

    private void clear() {
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                values[i][j] = 0;
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public double getChecked(int row, int column) {
        return values[row][column];
    }

    public double getChecked(Position pos) {
        return getChecked(pos.getRow(), pos.getColumn());
    }

    public void setValue(int row, int column, double data) {
        values[row][column] = data;
    }

    public void setValue(Position pos, double data) {
        setValue(pos.getRow(), pos.getColumn(), data);
    }

    public void print() {
        System.out.println();
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                System.out.printf("%4.1f\t",values[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }
}
