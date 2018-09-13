package org.silentpom.runner.algo.solve.commands;

import org.silentpom.runner.domain.CellType;
import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.maps.CommonMap;

/**
 * Created by Vlad on 09.09.2018.
 */
public class GravityFallsCommand implements GameCommand {

    @Override
    public String getCode() {
        return "FALLLLING";
    }

    @Override
    public Position moveOnly(Position x) {
        return x.down();
    }

    @Override
    public boolean moveInGame(CommonMap map, CommandResult result, boolean canKill) {
        Position x = result.getPosition();
        CellType cell = map.getCell(x);
        if (cell == CellType.LADDER || cell == CellType.PIPE) {
            return false;
        }

        Position down = result.getPosition().down();
        if (map.getCell(down).canStayOn()) {
            return false;
        }

        result.setPosition(down);
        return true;
    }
}
