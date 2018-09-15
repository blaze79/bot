package org.silentpom.runner.algo.estimation;

import org.silentpom.runner.algo.estimation.commands.DigCommandsSet;
import org.silentpom.runner.algo.estimation.commands.MoveCommandsSet;
import org.silentpom.runner.algo.estimation.policy.WeightPolicy;
import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.maps.ClearMap;
import org.silentpom.runner.domain.state.PositionAndCommand;

import java.util.List;

/**
 * Created by Vlad on 10.09.2018.
 */
public class DirectFiller extends AbstractFiller {

    public DirectFiller(ClearMap clearMap, WeightPolicy policy, List<Position> bots, Position hero) {
        super(clearMap, policy, bots, hero);
    }

    @Override
    protected List<PositionAndCommand> produceActiveCommands(Position position, ClearMap map) {
        return MoveCommandsSet.MOVE_COMMANDS.getNextCommands(
                position,
                clearMap
        );
    }

    @Override
    protected List<PositionAndCommand> produceDelayedCommands(Position position, ClearMap map) {
        return DigCommandsSet.DIG_COMMANDS.getNextCommands(
                position,
                clearMap
        );
    }


}
