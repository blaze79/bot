package org.silentpom.runner.algo.solve.commands;

import org.silentpom.runner.domain.CellCategory;
import org.silentpom.runner.domain.CellType;
import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.maps.CommonMap;

/**
 * Created by Vlad on 09.09.2018.
 *
 * dig left
 * move left
 * down to the hole
 * down to destination
 *
 */
public class DigLeftCommand implements GameCommand {

    @Override
    public String getCode() {
        return "DIG LEFT";
    }

    @Override
    public Position moveOnly(Position x) {
        return x;
    }
}
