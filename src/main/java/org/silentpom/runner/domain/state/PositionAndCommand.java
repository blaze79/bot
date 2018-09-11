package org.silentpom.runner.domain.state;

import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.commands.MoveCommand;

/**
 * Created by Vlad on 11.09.2018.
 */
public class PositionAndCommand {
    Position position;
    MoveCommand command;

    public PositionAndCommand(Position position, MoveCommand command) {
        this.position = position;
        this.command = command;
    }

    public Position getPosition() {
        return position;
    }

    public MoveCommand getCommand() {
        return command;
    }

    public static PositionAndCommand pair(Position position, MoveCommand command) {
        return new PositionAndCommand(position, command);
    }
}
