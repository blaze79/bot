package org.silentpom.runner.domain.actors;

import org.silentpom.runner.algo.solve.commands.GameCommand;
import org.silentpom.runner.domain.CellType;
import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.commands.MoveCommand;
import org.silentpom.runner.domain.state.HolesOfActor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vlad on 12.09.2018.
 */
public abstract class MovingObject extends CellObject {
    List<Position> history = new ArrayList<>();
    HolesOfActor actorHoles = new HolesOfActor();
    private int tickOfDead = -1;
    List<GameCommand> commandStack = new ArrayList<>();

    public MovingObject(Position position) {
        super(position);
    }

    public void startNewTick(Position position) {
        history.add(position);
        actorHoles.startNewTick(null);
        commandStack.add(null);
    }

    public void changePosition(Position position) {
        history.set(history.size() - 1, position);
    }

    public boolean wasInPosition(Position pos) {
        if(this.startPosition().equals(pos)) {
            return true;
        }

        for(int i=0; i< history.size(); ++i) {
            Position objectPosition = history.get(i);
            if(pos.equals(objectPosition)) {
                return true;
            }
        }

        return false;
    }

    public void changeHole(Position position) {
        actorHoles.changeLastHole(position);
    }

    public void changeCommand(GameCommand command) {
        commandStack.set(commandStack.size() - 1, command);
    }

    public void tickBack() {
        history.remove(history.size() - 1);
        if (history.size() > tickOfDead) {
            tickOfDead = -1;
        }
        actorHoles.tickBack();
        commandStack.remove(commandStack.size() - 1);
    }

    @Override
    public Position position(int time) {
        for (int i = history.size()-1; i >= 0; i--) {
            Position position = history.get(i);
            if (position != null) {
                return position;
            }
        }

        return startPosition();
    }

    public final CellType getCellType(int time, Position pos, boolean hide) {
        if (!hide && pos.equals(position(time))) {
            return getCellTypeImpl();
        }

        return actorHoles.cellType(pos);
    }

    protected abstract CellType getCellTypeImpl();


    public boolean isDead() {
        return tickOfDead >= 0;
    }

    public void setTickOfDead(int tickOfDead) {
        this.tickOfDead = tickOfDead;
    }

    public GameCommand getLastCommand() {
        return commandStack.get(commandStack.size() - 1);
    }
}
