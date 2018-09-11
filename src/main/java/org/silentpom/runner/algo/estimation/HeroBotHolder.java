package org.silentpom.runner.algo.estimation;

import org.silentpom.runner.domain.Position;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Vlad on 11.09.2018.
 */
public class HeroBotHolder {
    Set<Position> botSet = new HashSet<>();
    Position hero;

    int botsFound = 0;
    boolean heroFound = false;
    FillerState heroState = null;

    public HeroBotHolder(List<Position> bots, Position hero) {
        botSet.addAll(bots);
        this.hero = hero;
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
}
