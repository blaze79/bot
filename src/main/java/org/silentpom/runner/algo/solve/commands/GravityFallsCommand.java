package org.silentpom.runner.algo.solve.commands;

import org.silentpom.runner.domain.CellType;
import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.maps.CommonMap;

/**
 * Created by Vlad on 09.09.2018.
 */
public class GravityFallsCommand implements GameCommand {

    @Override
    public String getCode() {
        return "FALLLLING";
    }

    @Override
    public Position moveOnly(Position x) {
        return x.down();
    }
}
