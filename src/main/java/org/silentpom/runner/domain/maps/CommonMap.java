package org.silentpom.runner.domain.maps;

import org.silentpom.runner.domain.CellInfo;
import org.silentpom.runner.domain.CellType;
import org.silentpom.runner.domain.Position;

import java.util.List;

/**
 * Created by Vlad on 09.09.2018.
 */
public interface CommonMap {
    int rows();

    int columns();

    CellType getCell(int i, int j);

    default CellType getCell(Position p) {
        return getCell(p.getRow(), p.getColumn());
    }
}
