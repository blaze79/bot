package org.silentpom.runner.algo.solve.commands;

import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.maps.CommonMap;

/**
 * Created by Vlad on 09.09.2018.
 */
public class DieCommand implements GameCommand {

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

    @Override
    public boolean moveInGame(CommonMap map, CommandResult result, boolean canKill) {
        return true;
    }
}
