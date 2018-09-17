package org.silentpom.runner.domain.actors;

import org.silentpom.runner.domain.CellType;
import org.silentpom.runner.domain.Position;

/**
 * Created by Vlad on 12.09.2018.
 */
public class OtherBot extends MovingObject {

    public OtherBot(Position position) {
        super(position);
    }

    @Override
    protected CellType getCellTypeImpl() {
        if (isDead()) {
            return null;
        }
        return CellType.OTHER_HERO_LEFT;
    }
}
