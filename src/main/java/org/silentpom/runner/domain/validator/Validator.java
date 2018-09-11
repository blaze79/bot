package org.silentpom.runner.domain.validator;

import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.maps.CommonMap;

/**
 * Created by Vlad on 09.09.2018.
 */
public interface Validator {
    boolean isPositionValid(Position pos, CommonMap map);
}
