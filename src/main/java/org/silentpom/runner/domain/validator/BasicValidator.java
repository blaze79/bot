package org.silentpom.runner.domain.validator;

import org.silentpom.runner.domain.CellType;
import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.maps.CommonMap;

import static org.silentpom.runner.domain.CellCategory.isFreeCell;

/**
 * Created by Vlad on 09.09.2018.
 */
public abstract class BasicValidator implements Validator {

    @Override
    public final boolean isPositionValid(Position pos, CommonMap map) {
        if (pos.getRow() < 0 || pos.getRow() >= map.rows() || pos.getColumn() < 0 || pos.getColumn() >= map.columns()) {
            return false;
        }
        return isPositionValidImpl(pos, map);
    }

    public abstract boolean isPositionValidImpl(Position pos, CommonMap map);
}
