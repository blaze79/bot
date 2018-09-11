package org.silentpom.runner.algo.estimation;

import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.commands.MoveCommand;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vlad on 10.09.2018.
 */
public class FillerState {
    Position position;
    FillerState parent;
    double value;
    int generation;
    MoveCommand command;

    boolean delayed = false;
    int delayCounter = 0;

    public FillerState(Position position, double value) {
        this.position = position;
        this.parent = null;
        this.value = value;
        this.generation = 0;
        this.command = null;
    }

    private FillerState(Position position, FillerState parent, double value, int generation) {
        this.position = position;
        this.parent = parent;
        this.value = value;
        this.generation = generation;
        this.command = null;
    }

    private FillerState(Position position, FillerState parent, MoveCommand command, double value, int generation) {
        this.position = position;
        this.parent = parent;
        this.value = value;
        this.generation = generation;
        this.command = command;
    }

    public Position getPosition() {
        return position;
    }

    public FillerState getParent() {
        return parent;
    }

    public double getValue() {
        return value;
    }

    public int getGeneration() {
        return generation;
    }

    public MoveCommand getCommand() {
        return command;
    }

    public FillerState makeChildren(Position pos, double w) {
        return new FillerState(
                pos,
                this,
                w,
                generation + 1
        );
    }

    public FillerState makeChildren(Position pos, MoveCommand command, double w) {
        return new FillerState(
                pos,
                this,
                command,
                w,
                generation + 1
        );
    }

    public FillerState makeDelayChildren(Position pos, MoveCommand command, double w) {
        FillerState child = new FillerState(
                pos,
                this,
                command,
                w,
                generation + command.tickCount()
        );
        child.delayed = true;
        child.delayCounter = command.tickCount() - 1;

        return child;
    }

    public void print() {
        System.out.println();
        for(FillerState chain = this; chain!=null; chain = chain.getParent()) {
            if(chain.getCommand() !=null) {
                System.out.println(chain.getCommand().getCode());
            }
        }
    }

    public List<FillerState> asList() {
        List<FillerState> list = new ArrayList<>();
        for(FillerState chain = this; chain!=null; chain = chain.getParent()) {
            list.add(chain);
        }
        return list;
    }

    public boolean isDelayed() {
        return delayed;
    }

    public boolean makeDelayedStep() {
        if(delayed && delayCounter>0) {
            delayCounter--;
            return (delayCounter==0);
        }

        return true;
    }
}
