package org.silentpom.runner.domain.commands;

import org.silentpom.runner.algo.solve.commands.GameCommand;
import org.silentpom.runner.algo.solve.commands.GravityFallsCommand;
import org.silentpom.runner.domain.CellType;
import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.maps.CommonMap;

/**
 * Created by Vlad on 09.09.2018.
 */
public class FallingCommand implements MoveCommand {
    @Override
    public Position moveCommand(Position x, CommonMap map) {
        if(map.getCell(x) == CellType.LADDER || map.getCell(x) == CellType.PIPE) {
            return null;
        }

        if (map.getCell(x.down()).canStayOn()) {
            return null;
        }

        return x.down();
    }

    @Override
    public String getCode() {
        return "FALLLLING";
    }

    @Override
    public GameCommand toGameCommand() {
        return new GravityFallsCommand();
    }
}
