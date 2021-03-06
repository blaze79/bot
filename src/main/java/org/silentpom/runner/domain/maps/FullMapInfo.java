package org.silentpom.runner.domain.maps;

import org.silentpom.runner.domain.CellCategory;
import org.silentpom.runner.domain.CellType;
import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.actors.HoleCell;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Vlad on 09.09.2018.
 */
public class FullMapInfo {
    SimpleMap simple;
    ClearMap clearMap;
    Position hero;
    List<Position> bots;
    List<Position> enemy;
    List<Position> gold;
    List<HoleCell> holes;

    public SimpleMap getSimple() {
        return simple;
    }

    public ClearMap getClearMap() {
        return clearMap;
    }

    public Position getHero() {
        return hero;
    }

    public void fakeHero(Position hero) {
        this.hero = hero;
    }

    public List<Position> getBots() {
        return bots;
    }

    public List<Position> getEnemy() {
        return enemy;
    }

    public List<Position> getGold() {
        return gold;
    }

    public List<HoleCell> getHoles() {
        return holes;
    }

    public static FullMapInfo buildFromMap(SimpleMap map) {
        FullMapInfo fullMapInfo = new FullMapInfo();
        fullMapInfo.buildFrom(map);
        return fullMapInfo;
    }

    private void buildFrom(SimpleMap map) {
        simple = map;
        clearMap = ClearMap.fromMap(simple);

        gold = simple.selectCells(cell -> cell == CellType.GOLD)
                .stream()
                .map(info -> info.getPosition())
                .collect(Collectors.toList());

        bots = simple.selectCells(cell -> cell.getCategory() == CellCategory.BOT)
                .stream()
                .map(info -> info.getPosition())
                .collect(Collectors.toList());

        enemy = simple.selectCells(cell -> cell.getCategory() == CellCategory.ENEMY && !CellType.ENEMY_PIT.equals(cell))
                .stream()
                .map(info -> info.getPosition())
                .collect(Collectors.toList());

        holes = simple.selectCells(cell -> cell.getCategory() == CellCategory.HOLE)
                .stream()
                .map(info -> new HoleCell(info.getPosition(), info.getType()))
                .collect(Collectors.toList());


        List<Position> heros = simple.selectCells(cell -> cell.getCategory() == CellCategory.MY)
                .stream()
                .map(info -> info.getPosition())
                .collect(Collectors.toList());

        if (heros.size() == 0) {
            hero = null;
        } else if (heros.size() == 1) {
            hero = heros.get(0);
        } else {
            throw new RuntimeException("Too many heros: " + heros.size());
        }

    }
}
