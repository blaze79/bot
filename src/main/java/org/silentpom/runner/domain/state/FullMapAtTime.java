/*
 * Copyright 2018 Russian Post
 *
 * This source code is Russian Post Confidential Proprietary.
 * This software is protected by copyright. All rights and titles are reserved.
 * You shall not use, copy, distribute, modify, decompile, disassemble or reverse engineer the software.
 * Otherwise this violation would be treated by law and would be subject to legal prosecution.
 * Legal use of the software provides receipt of a license from the right holder only.
 */
package org.silentpom.runner.domain.state;

import org.silentpom.runner.algo.solve.commands.CommandResult;
import org.silentpom.runner.algo.solve.commands.GameCommand;
import org.silentpom.runner.algo.solve.commands.GravityFallsCommand;
import org.silentpom.runner.domain.CellCategory;
import org.silentpom.runner.domain.CellInfo;
import org.silentpom.runner.domain.CellType;
import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.actors.Hero;
import org.silentpom.runner.domain.actors.Hunter;
import org.silentpom.runner.domain.actors.MovingObject;
import org.silentpom.runner.domain.actors.OtherBot;
import org.silentpom.runner.domain.maps.CellFilter;
import org.silentpom.runner.domain.maps.ClearMap;
import org.silentpom.runner.domain.maps.CommonMap;
import org.silentpom.runner.domain.maps.FullMapInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FullMapAtTime {
    ClearMap clearMap;

    GoldOfMap goldOfMap;
    Hero hero;
    List<Hunter> hunters;
    List<OtherBot> bots;
    CommonHoles commonHoles;
    int tick = 0;
    int WINDOW = 10;

    CommonMap simpleView;
    CommonMap heroView;
    List<CommonMap> huntersView;
    List<CommonMap> botsView;


    public FullMapAtTime(FullMapInfo info) {
        Position center = info.getHero();
        hero = new Hero(info.getHero());
        hunters = info.getEnemy().stream()
                .filter(x -> x.absDistance(center) < WINDOW)
                .map(x -> new Hunter(x))
                .collect(Collectors.toList());
        bots = info.getBots()
                .stream()
                .filter(x -> x.absDistance(center) < WINDOW)
                .map(x -> new OtherBot(x))
                .collect(Collectors.toList());
        commonHoles = new CommonHoles(info.getHoles());
        //goldOfMap = new GoldOfMap(info, holeActors());
        goldOfMap = new GoldOfMap(info, hero);

        clearMap = info.getClearMap();

        createCommonMaps();
    }

    private int holeActors() {
        return 1 + bots.size();
    }

    private CellType findCellType(Position pos, boolean hideHero, int hidedBot, int hidedHunter) {
        CellType cellType = clearMap.getCell(pos);
        CellType holeCell = null;

        if (cellType == CellType.UNDESTROYABLE_WALL) {
            return cellType;
        }

        cellType = hero.getCellType(tick, pos, hideHero);
        if (cellType != null) {
            if (cellType.getCategory() == CellCategory.HOLE) {
                holeCell = cellType;
                cellType = null;
            } else {
                return cellType;
            }
        }

        for (int i = 0; i < bots.size(); ++i) {
            cellType = bots.get(i).getCellType(tick, pos, hidedBot == i);
            if (cellType != null) {
                if (cellType.getCategory() == CellCategory.HOLE) {
                    holeCell = cellType;
                    cellType = null;
                } else {
                    return cellType;
                }
            }
        }

        for (int i = 0; i < hunters.size(); ++i) {
            cellType = hunters.get(i).getCellType(tick, pos, hidedHunter == i);
            if (cellType != null) {
                if (cellType.getCategory() == CellCategory.HOLE) {
                    holeCell = cellType;
                    cellType = null;
                } else {
                    return cellType;
                }
            }
        }

        if (holeCell != null) {
            return holeCell;
        }

        cellType = commonHoles.cellType(pos);
        if (cellType != null) {
            return cellType;
        }

        if (goldOfMap.checkGold(pos)) {
            return CellType.GOLD;
        }

        return clearMap.getCell(pos);
    }

    public void newTick() {
        tick++;

        goldOfMap.newTick();
        commonHoles.startNewTick();

        CommandResult tempResult = new CommandResult();

        hero.startNewTick(null);
        initCommand(hero, heroView, tempResult);

        for (int i = 0; i < bots.size(); ++i) {
            bots.get(i).startNewTick(null);
            initCommand(bots.get(i), botsView.get(i), tempResult);

        }

        for (int i = 0; i < hunters.size(); ++i) {
            hunters.get(i).startNewTick(null);
            initCommand(hunters.get(i), huntersView.get(i), tempResult);
        }
    }

    /*public void initCommands() {
        CommandResult tempResult = new CommandResult();
        initCommand(hero, heroView, tempResult);

        for (int i = 0; i < bots.size(); ++i) {
            initCommand(bots.get(i), botsView.get(i), tempResult);

        }

        for (int i = 0; i < hunters.size(); ++i) {
            initCommand(hunters.get(i), huntersView.get(i), tempResult);
        }
    }*/


    private void initCommand(MovingObject object, CommonMap mapView, CommandResult tempResult) {
        tempResult.resetPosition(object.position(tick));

        boolean willFall = GRAVITY.moveInGame(mapView, tempResult, false);
        if (willFall) {
            object.changeCommand(GRAVITY);
        } else {
            object.changeCommand(null);
        }
    }

    public void tickBack() {
        tick--;

        goldOfMap.tickBack();
        commonHoles.tickBack();
        hero.tickBack();

        for (Hunter hunter : hunters) {
            hunter.tickBack();
        }

        for (OtherBot bot : bots) {
            bot.tickBack();
        }
    }

    public CellType processHeroMove(Position newPos, CommandResult result) {
        CellType old = heroView.getCell(newPos);
        hero.changePosition(newPos);
        hero.changeHole(result.getHole());
        return old;
    }

    private CommonMap createCommonMap(boolean hideHero, int hidedBot, int hidedHunter) {
        return new CommonMap() {
            @Override
            public int rows() {
                return clearMap.rows();
            }

            @Override
            public int columns() {
                return clearMap.rows();
            }

            @Override
            public CellType getCell(int i, int j) {
                return findCellType(PositionsCache.make(i, j), hideHero, hidedBot, hidedHunter);
            }

            @Override
            public CellType getCell(Position p) {
                return findCellType(p, hideHero, hidedBot, hidedHunter);
            }
        };
    }

    private void createCommonMaps() {
        simpleView = createCommonMap(false, -1, -1);
        heroView = createCommonMap(true, -1, -1);

        huntersView = new ArrayList<>();
        for (int i = 0; i < hunters.size(); ++i) {
            huntersView.add(createCommonMap(false, -1, i));
        }

        botsView = new ArrayList<>();
        for (int i = 0; i < bots.size(); ++i) {
            botsView.add(createCommonMap(false, i, -1));
        }
    }

    public Hero getHero() {
        return hero;
    }

    public List<Hunter> getHunters() {
        return hunters;
    }

    public CommonMap getHeroView() {
        return heroView;
    }

    public CommonMap getSimpleMap() {
        return simpleView;
    }

    public ClearMap getClearMap() {
        return clearMap;
    }

    public static GameCommand GRAVITY = new GravityFallsCommand();

}
