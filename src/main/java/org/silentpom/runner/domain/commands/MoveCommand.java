package org.silentpom.runner.domain.commands;

import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.maps.CommonMap;

/**
 * Created by Vlad on 09.09.2018.
 */
public interface MoveCommand {
    Position moveCommand(Position x, CommonMap map);

    String getCode();

    Position moveOnly(Position x);

    default int tickCount() {
        return 1;
    }

    default boolean isDead() {
        return false;
    };
}
