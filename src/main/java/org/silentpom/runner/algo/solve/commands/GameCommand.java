package org.silentpom.runner.algo.solve.commands;

import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.maps.CommonMap;

/**
 * Created by Vlad on 09.09.2018.
 */
public interface GameCommand {

    String getCode();

    Position moveOnly(Position x);

    default boolean isDead() {
        return false;
    };
}
