package org.silentpom.runner.algo.solve.commands;

import org.silentpom.runner.domain.Position;

/**
 * Created by Vlad on 09.09.2018.
 */
public class DoNothingCommand implements GameCommand {

    @Override
    public String getCode() {
        return "WAIT";
    }

    @Override
    public Position moveOnly(Position x) {
        return x;
    }
}
