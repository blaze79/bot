package org.silentpom.runner.algo.estimation.commands;

import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.commands.*;
import org.silentpom.runner.domain.maps.CommonMap;

import java.util.stream.Stream;

/**
 * Created by Vlad on 09.09.2018.
 */
public class DigCommandsSet extends AbstractCommandsSet {
    MoveCommand digCommands[] = {
            new DigLeftEstimationCommand(),
            new DigRightEstimationCommand()
    };

    @Override
    protected MoveCommand[] commands() {
        return digCommands;
    }

    @Override
    protected Stream<Position> prevPositions(Position pos, CommonMap clearMap) {
        return Stream.of(
                pos.up().up().left(),
                pos.up().up().right()
        );
    }

    public static DigCommandsSet DIG_COMMANDS = new DigCommandsSet();
}
