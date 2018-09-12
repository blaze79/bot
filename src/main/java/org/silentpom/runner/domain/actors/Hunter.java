package org.silentpom.runner.domain.actors;

import org.silentpom.runner.domain.CellType;
import org.silentpom.runner.domain.Position;

/**
 * Created by Vlad on 12.09.2018.
 */
public class Hunter  extends MovingObject {
    public Hunter(Position position) {
        super(position);
    }

    @Override
    public CellType getCellType(int time) {
        if (isDead()) {
            return null;
        }
        return CellType.ENEMY_LEFT;
    }
}
