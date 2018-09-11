package org.silentpom.runner.domain;

import javafx.scene.control.Cell;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vlad on 09.09.2018.
 */
public enum CellType {
    /**
     * Пустое место – по которому может двигаться герой
     */
    NONE(' ', CellCategory.FREE),

    /**
     * Cтена в которой можно просверлить дырочку слева или справа от героя (в зависимости от того, куда он сейчас смотрит):
     */
    BRICK('#', CellCategory.WALL),

    /**
     * Стена со временем зарастает. Когда процесс начинается - мы видим таймер:
     */
    PIT_FILL_1('1', CellCategory.HOLE),
    PIT_FILL_2('2', CellCategory.HOLE),
    PIT_FILL_3('3', CellCategory.HOLE),
    PIT_FILL_4('4', CellCategory.HOLE),
    /**
     * В момент сверления мы видим процесс так:
     */
    DRILL_PIT('*', CellCategory.HOLE),

    /**
     * Неразрушаемая стена - в ней ничего просверлить не получится:
     */
    UNDESTROYABLE_WALL('☼', CellCategory.WALL),


    /**
     * Золото - его надо собирать:
     */
    GOLD('$', CellCategory.GOLD),

    /**
     * Лестница и труба - по ним можно перемещаться по уровню:
     */
    LADDER('H', CellCategory.STAIRS),
    PIPE('~', CellCategory.STAIRS),

    /**
     * Твой герой в зависимости от того, чем он сейчас занят отображается следующими символами:
     */
    HERO_DIE('Ѡ', CellCategory.MY),
    HERO_DRILL_LEFT('Я', CellCategory.MY),
    HERO_DRILL_RIGHT('R', CellCategory.MY),
    HERO_LADDER('Y', CellCategory.MY),
    HERO_LEFT('◄', CellCategory.MY),
    HERO_RIGHT('►', CellCategory.MY),
    HERO_FALL_LEFT(']', CellCategory.MY),
    HERO_FALL_RIGHT('[', CellCategory.MY),
    HERO_PIPE_LEFT('{', CellCategory.MY),
    HERO_PIPE_RIGHT('}', CellCategory.MY),

    /**
     * Герои других игроков, соответственно
     */
    OTHER_HERO_DIE('Z', CellCategory.BOT),
    OTHER_HERO_LEFT(')', CellCategory.BOT),
    OTHER_HERO_RIGHT('(', CellCategory.BOT),
    OTHER_HERO_LADDER('U', CellCategory.BOT),
    OTHER_HERO_PIPE_LEFT('Э', CellCategory.BOT),
    OTHER_HERO_PIPE_RIGHT('Є', CellCategory.BOT),

    /**
     * Так же и враг-охотник
     */
    ENEMY_LADDER('Q', CellCategory.ENEMY),
    ENEMY_LEFT('«', CellCategory.ENEMY),
    ENEMY_RIGHT('»', CellCategory.ENEMY),
    ENEMY_PIPE_LEFT('<', CellCategory.ENEMY),
    ENEMY_PIPE_RIGHT('>', CellCategory.ENEMY),
    ENEMY_PIT('X', CellCategory.ENEMY);

    char code;
    CellCategory category;

    CellType(char code, CellCategory category) {
        this.code = code;
        this.category = category;
    }

    public char getCode() {
        return code;
    }

    public CellCategory getCategory() {
        return category;
    }

    private static Map<Character, CellType> mapper;

    static {
        mapper = new HashMap<>();
        for (CellType value : CellType.values()) {
            mapper.put(value.code, value);
        }
    }

    public static CellType fromChar(char c) {
        CellType cellType = mapper.get(c);
        assert (cellType != null);
        return cellType;
    }

    public boolean canStayOn() {
        if (this == NONE) {
            return false;
        }

        if (this == GOLD) {
            return false;
        }

        if (getCategory() == CellCategory.HOLE) {
            return false;
        }

        if(this == PIPE) {
            return false;
        }

        return true;
    }

}
