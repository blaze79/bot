package org.silentpom.runner.domain.actors;

import org.silentpom.runner.domain.CellType;
import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.commands.MoveCommand;

import java.util.List;

/**
 * Created by Vlad on 12.09.2018.
 */
public class Hero extends MovingObject {

    public Hero(Position position) {
        super(position);
    }

    @Override
    public CellType getCellType(int time) {
        if (isDead()) {
            return null;
        }
        return CellType.HERO_LEFT;
    }
}
