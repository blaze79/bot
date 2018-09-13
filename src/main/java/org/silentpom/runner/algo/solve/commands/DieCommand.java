package org.silentpom.runner.algo.solve.commands;

import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.maps.CommonMap;

/**
 * Created by Vlad on 09.09.2018.
 */
public class DieCommand implements MoveCommand {
    @Override
    public Position moveCommand(Position x, CommonMap map) {
        return x;
    }

    @Override
    public String getCode() {
        return "DIEEEE";
    }

    @Override
    public Position moveOnly(Position x) {
        return x;
    }

    @Override
    public boolean isDead() {
        return true;
    }
}
