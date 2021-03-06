package org.silentpom.runner.algo.estimation;

import org.silentpom.runner.algo.estimation.commands.DigCommandsSet;
import org.silentpom.runner.algo.estimation.policy.WeightPolicy;
import org.silentpom.runner.domain.Position;
import org.silentpom.runner.algo.estimation.commands.MoveCommandsSet;
import org.silentpom.runner.domain.state.PositionAndCommand;
import org.silentpom.runner.domain.maps.ClearMap;
import org.silentpom.runner.domain.masks.BitMask;
import org.silentpom.runner.domain.masks.DoubleMask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vlad on 10.09.2018.
 */
public class BackFiller extends AbstractFiller {

    public BackFiller(ClearMap clearMap, WeightPolicy policy, List<Position> bots, Position hero) {
        super(clearMap, policy, bots, hero);
    }

    @Override
    protected List<PositionAndCommand> produceActiveCommands(Position position, ClearMap map) {
        return MoveCommandsSet.MOVE_COMMANDS.getPreviousCommands(
                position,
                clearMap
        );
    }

    @Override
    protected List<PositionAndCommand> produceDelayedCommands(Position position, ClearMap map) {
        return DigCommandsSet.DIG_COMMANDS.getPreviousCommands(
                position,
                clearMap
        );
    }


}
