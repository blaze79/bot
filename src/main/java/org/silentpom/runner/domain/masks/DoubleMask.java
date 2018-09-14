package org.silentpom.runner.domain.masks;

import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.state.PositionsCache;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

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

    public void addWithWeight(DoubleMask mask, double weight) {
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                values[i][j] = values[i][j] + mask.values[i][j] * weight;
            }
        }
    }

    public void print() {
        System.out.println();
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                System.out.printf("%4.1f\t", values[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    private char convertSymbol(int part) {
        if (part < 0) {
            return 'X';
        }
        if (part == 0) {
            return ' ';
        }
        if (part >= 8) {
            return '▉';
        }

        char start = 0x2581;
        char fix = (char) (part - 1);
        return (char) (start + fix);
    }

    private char convertSymbolDigit(int part) {
        if (part < 0) {
            return 'X';
        }

        if (part > 15) {
            return 'F';
        }

        return Character.toUpperCase(Character.forDigit(part, 16));
    }

    private double maxValue() {
        return Stream.of(values).flatMapToDouble(row -> DoubleStream.of(row)).max().orElse(0);
    }

    private int mapToInt(int maxInt, double maxDouble, double value) {
        if (value == 0.0) {
            return -1;
        }
        return (int) Math.floor(value / maxDouble * maxInt + 0.5);
    }

    public String getStringView() {
        StringBuilder builder = new StringBuilder((rows + 2) * (columns + 5));
        double max = maxValue();

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                builder.append(
                        convertSymbolDigit(
                                mapToInt(15, max, values[i][j])
                        )
                );
            }
            builder.append('\n');
        }
        return builder.toString();
    }

    public List<Position> findMaximum() {
        List<Position> maxx = new ArrayList<>();
        double gap = maxValue() / 16;
        for (int i = 1; i < rows - 1; ++i) {
            for (int j = 1; j < columns - 1; ++j) {
                if (values[i][j] > gap) {
                    double val = values[i][j];
                    if (val >= values[i - 1][j] && val >= values[i + 1][j] && val >= values[i][j - 1] && val >= values[i][j + 1]) {
                        maxx.add(PositionsCache.make(i, j));
                    }
                }
            }
        }

        return maxx;
    }

    public boolean checkLocalMaximum(Position pos) {
        double val = getChecked(pos);
        double maxValue = Stream.of(
                pos.left(), pos.right(), pos.up(), pos.down(), pos.left(),
                pos.left().up(), pos.left().down(), pos.right().up(), pos.right().down()
        ).mapToDouble(x -> getChecked(x)).max().orElse(0);

        return val>= maxValue;
    }

}
