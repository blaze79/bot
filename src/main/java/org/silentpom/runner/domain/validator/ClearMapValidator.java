package org.silentpom.runner.domain.validator;

import org.silentpom.runner.domain.CellType;
import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.maps.CommonMap;

import static org.silentpom.runner.domain.CellCategory.isFreeCell;

/**
 * Created by Vlad on 09.09.2018.
 */
public class ClearMapValidator extends BasicValidator {

    @Override
    public boolean isPositionValidImpl(Position pos, CommonMap map) {
        CellType cell = map.getCell(pos);
        return  isFreeCell(cell.getCategory());
    }
}
