package org.silentpom.runner.domain.actors;

import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.commands.MoveCommand;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vlad on 12.09.2018.
 */
public abstract class MovingObject extends CellObject {
    List<MoveCommand> commandStack = new ArrayList<>();

    public MovingObject(Position position) {
        super(position);
    }

    public void startNewTick(MoveCommand command) {
        commandStack.add(command);
    }

    public void changeLastCommand(MoveCommand command) {
        commandStack.set(commandStack.size() -1, command);
    }

    public void tickBack() {
        commandStack.remove(commandStack.size() -1);
    }

    @Override
    public Position position(int time) {
        Position pos = startPosition();
        for(MoveCommand command: commandStack) {
            pos = command.moveOnly(pos);
        }
        return pos;
    }

    public boolean isDead() {
        return commandStack.stream().anyMatch( x -> x.isDead());
    }
}
