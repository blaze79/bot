package org.silentpom.runner.algo.solve.commands;

import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.maps.CommonMap;

/**
 * Created by Vlad on 09.09.2018.
 */
public interface GameCommand {

    /**
     * reduce memory consumption here
     * @param x
     * @param map
     * @param result
     * @param canKill
     * @return
     */
    boolean moveInGame(CommonMap map, CommandResult result, boolean canKill);

    String getCode();

    Position moveOnly(Position x);

    default boolean isDead() {
        return false;
    };
}
