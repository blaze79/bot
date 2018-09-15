package org.silentpom.runner.algo.estimation;

import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.masks.BitMask;
import org.silentpom.runner.domain.masks.DoubleMask;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Vlad on 11.09.2018.
 */
public class FillerResultHolder {
    Set<Position> botSet = new HashSet<>();
    Position hero;
    BitMask mask;
    DoubleMask result;
    List<Position> markedPoints = new ArrayList<>(32);

    int botsFound = 0;
    boolean heroFound = false;
    FillerState heroState = null;

    public FillerResultHolder(List<Position> bots, Position hero, BitMask mask, DoubleMask result) {
        botSet.addAll(bots);
        this.hero = hero;
        this.mask = mask;
        this.result = result;
    }

    public void checkOrder(FillerState state) {
        Position pos = state.getPosition();
        if (!heroFound) {
            if(pos.equals(hero)) {
                heroFound = true;
                heroState = state;
                return;
            }

            if(botSet.contains(pos)) {
                botsFound++;
                botSet.remove(pos);
            }

        }
    }

    public int getBotsFound() {
        return botsFound;
    }

    public boolean isHeroFound() {
        return heroFound;
    }

    public FillerState getHeroState() {
        return heroState;
    }

    public BitMask getMask() {
        return mask;
    }

    public DoubleMask getResult() {
        return result;
    }

    public void markCell(Position pos) {
        mask.setChecked(pos, true);
        markedPoints.add(pos);
    }

    public int getMarks() {
        return markedPoints.size();
    }

    public List<Position> getMarkedPoints() {
        return markedPoints;
    }
}
