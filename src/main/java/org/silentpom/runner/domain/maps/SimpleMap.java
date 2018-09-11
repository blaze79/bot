package org.silentpom.runner.domain.maps;

import org.silentpom.runner.domain.CellInfo;
import org.silentpom.runner.domain.CellType;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vlad on 09.09.2018.
 */
public class SimpleMap implements CommonMap {
    int rows;
    int columns;
    CellType[][] cells;

    public SimpleMap(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        cells = new CellType[rows][columns];

        clear();
    }

    private SimpleMap(int rows, int columns, CellType[][] rowCells) {
        this.rows = rows;
        this.columns = columns;
        cells = rowCells;
    }

    private void clear() {
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                cells[i][j] = CellType.NONE;
            }
        }
    }

    public int rows() {
        return rows;
    }

    public int columns() {
        return columns;
    }

    public CellType getCell(int i, int j) {
        return cells[i][j];
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

    public void setCell(int i, int j, CellType type) {
        cells[i][j] = type;
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

    public String getStringView() {
        StringBuilder builder = new StringBuilder((rows + 2)*(columns+5));
        builder.append('\n');
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                builder.append(cells[i][j].getCode());
            }
            builder.append('\n');
        }
        return builder.append('\n').toString();
    }

    public static SimpleMap fromFile(InputStreamReader reader) throws IOException {
        List<String> arr = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(reader)) {
            String strLine;
            while ((strLine = br.readLine()) != null) {
                arr.add(strLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        SimpleMap map = new SimpleMap(arr.size(), arr.get(0).length());
        for (int i = 0; i < arr.size(); ++i) {
            for (int j = 0; j < arr.get(i).length(); ++j) {
                map.setCell(i, j, CellType.fromChar(
                        arr.get(i).charAt(j)
                ));
            }
        }

        return map;
    }

    public static SimpleMap fromClearMap(ClearMap map) {
        return new SimpleMap(map.rows(), map.columns(), map.copyMap());
    }
}
