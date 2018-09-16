package org.silentpom.runner.domain.actors;

import org.silentpom.runner.algo.solve.commands.GameCommand;
import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.commands.MoveCommand;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vlad on 12.09.2018.
 */
public abstract class MovingObject extends CellObject {
    List<Position> history = new ArrayList<>();
    private int tickOfDead = -1;

    public MovingObject(Position position) {
        super(position);
    }

    public void startNewTick(Position position) {
        history.add(position);
    }

    public void changePosition(Position position) {
        history.set(history.size() - 1, position);
    }

    public void tickBack() {
        history.remove(history.size() - 1);
        if(history.size() > tickOfDead) {
            tickOfDead = -1;
        }
    }

    @Override
    public Position position(int time) {
        for (int i = history.size(); i >= 0; i--) {
            Position position = history.get(i);
            if (position != null) {
                return position;
            }
        }

        return history.get(history.size() - 1);
    }

    public boolean isDead() {
        return tickOfDead >= 0;
    }

    public void setTickOfDead(int tickOfDead) {
        this.tickOfDead = tickOfDead;
    }
}
