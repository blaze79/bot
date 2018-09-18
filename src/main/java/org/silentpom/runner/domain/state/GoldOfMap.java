package org.silentpom.runner.domain.state;

import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.actors.Hero;
import org.silentpom.runner.domain.maps.FullMapInfo;
import org.silentpom.runner.domain.masks.BitMask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vlad on 16.09.2018.
 */
public class GoldOfMap {
    BitMask mask;
    //List<List<Position>> objects;
    Hero hero;

    /*public GoldOfMap(FullMapInfo info, int objectsCount) {
        mask = new BitMask(info.getSimple().rows(), info.getSimple().columns());
        for (Position pos : info.getGold()) {
            mask.setChecked(pos, true);
        }

        objects = new ArrayList<>();
        for (int i = 0; i < objectsCount; ++i) {
            objects.add(new ArrayList<>(16));
        }
    }*/

    public GoldOfMap(FullMapInfo info, Hero hero) {
        mask = new BitMask(info.getSimple().rows(), info.getSimple().columns());
        for (Position pos : info.getGold()) {
            mask.setChecked(pos, true);
        }
        this.hero = hero;
    }

    private boolean isGold(Position pos) {
        return mask.getChecked(pos);
    }

    public boolean checkGold(Position pos) {
        if (isGold(pos) == false) {
            return false;
        }

        return !hero.wasInPosition(pos);

        /*return !objects.stream()
                .flatMap(list -> list.stream())
                .filter(x -> pos.equals(x))
                .findAny()
                .isPresent();*/
    }

    public void newTick() {
        //objects.forEach(list -> list.add(null));
    }

    public void tickBack() {
        //objects.forEach(list -> list.remove(list.size() - 1));
    }

    /*public boolean objectMoved(int object, Position pos) {
        List<Position> positionList = objects.get(object);

        positionList.set(positionList.size() - 1, null);
        if(pos == null) {
            return false;
        }

        if (checkGold(pos)) {
            positionList.set(positionList.size() - 1, pos);
            return true;
        } else {
            return false;
        }
    }*/

}
