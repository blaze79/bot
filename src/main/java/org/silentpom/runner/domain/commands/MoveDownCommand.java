package org.silentpom.runner.domain.commands;

import org.silentpom.runner.algo.solve.commands.GameCommand;
import org.silentpom.runner.algo.solve.commands.GameDownCommand;
import org.silentpom.runner.domain.CellType;
import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.maps.CommonMap;

/**
 * Created by Vlad on 09.09.2018.
 */
public class MoveDownCommand implements MoveCommand {
    @Override
    public Position moveCommand(Position x, CommonMap map) {
        if (map.getCell(x) == CellType.LADDER || map.getCell(x) == CellType.PIPE) {
            return x.down();
        }
        if (map.getCell(x.down()) == CellType.LADDER) {
            return x.down();
        }
        return null;
    }

    @Override
    public String getCode() {
        return "DOWN";
    }

    @Override
    public GameCommand toGameCommand() {
        return new GameDownCommand();
    }
}
