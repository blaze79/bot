package org.silentpom.runner.domain.maps;

import org.silentpom.runner.domain.CellType;

/**
 * Created by Vlad on 09.09.2018.
 */
public interface CellFilter {
    boolean match(CellType type);
}
