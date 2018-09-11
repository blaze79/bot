package org.silentpom.runner.algo.estimation.commands;

import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.commands.*;
import org.silentpom.runner.domain.maps.CommonMap;

import java.util.stream.Stream;

/**
 * Created by Vlad on 09.09.2018.
 */
public class MoveCommandsSet extends AbstractCommandsSet {
    MoveCommand moveCommands[] = {
            new MoveLeftCommand(),
            new MoveRightCommand(),
            new MoveUpCommand(),
            new MoveDownCommand(),
            new FallingCommand()
    };

    @Override
    protected MoveCommand[] commands() {
        return moveCommands;
    }

    @Override
    protected Stream<Position> prevPositions(Position pos, CommonMap clearMap) {
        return Stream.of(
                pos.left(),
                pos.right(),
                pos.up(),
                pos.down()
        );
    }

    public static MoveCommandsSet MOVE_COMMANDS = new MoveCommandsSet();
}
