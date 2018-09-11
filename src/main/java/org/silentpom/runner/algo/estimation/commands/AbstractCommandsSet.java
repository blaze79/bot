package org.silentpom.runner.algo.estimation.commands;

import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.commands.MoveCommand;
import org.silentpom.runner.domain.maps.ClearMap;
import org.silentpom.runner.domain.maps.CommonMap;
import org.silentpom.runner.domain.state.PositionAndCommand;
import org.silentpom.runner.domain.validator.ClearMapValidator;
import org.silentpom.runner.domain.validator.Validator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Vlad on 09.09.2018.
 */
public abstract class AbstractCommandsSet {

    protected abstract MoveCommand[] commands();

    protected abstract Stream<Position> prevPositions(Position pos, CommonMap clearMap);

    public List<Position> getPreviousPoints(Position pos, CommonMap clearMap, Validator validator) {
        return prevPositions(pos, clearMap).filter(
                x -> checkFromTo(x, pos, clearMap, validator)
        ).collect(Collectors.toList());
    }

    public List<Position> getPreviousPoints(Position pos, ClearMap clearMap) {
        return getPreviousPoints(pos, clearMap, VALIDATOR);
    }

    private boolean checkFromTo(Position from, Position to, CommonMap clearMap, Validator validator) {
        if (!validator.isPositionValid(from, clearMap)) {
            return false;
        }


        for (MoveCommand command : commands()) {
            Position position = command.moveCommand(from, clearMap);
            if (to.equals(position)) {
                return true;
            }
        }

        return false;
    }

    public List<PositionAndCommand> getPreviousCommands(Position pos, CommonMap clearMap, Validator validator) {
        return prevPositions(pos, clearMap).map(
                x -> findCommand(x, pos, clearMap, validator)
        ).filter(x -> x != null)
                .collect(Collectors.toList());
    }

    public List<PositionAndCommand> getPreviousCommands(Position pos, ClearMap clearMap) {
        return getPreviousCommands(pos, clearMap, VALIDATOR);
    }

    private PositionAndCommand findCommand(Position from, Position to, CommonMap clearMap, Validator validator) {
        if (!validator.isPositionValid(from, clearMap)) {
            return null;
        }

        for (MoveCommand command : commands()) {
            Position position = command.moveCommand(from, clearMap);
            if (to.equals(position)) {
                return PositionAndCommand.pair(from, command);
            }
        }

        return null;
    }


    public static ClearMapValidator VALIDATOR = new ClearMapValidator();
}
