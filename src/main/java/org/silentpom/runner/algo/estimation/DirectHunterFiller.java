package org.silentpom.runner.algo.estimation;

import org.silentpom.runner.algo.estimation.commands.DigCommandsSet;
import org.silentpom.runner.algo.estimation.commands.MoveCommandsSet;
import org.silentpom.runner.algo.estimation.policy.WeightPolicy;
import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.maps.ClearMap;
import org.silentpom.runner.domain.state.PositionAndCommand;

import java.util.Collections;
import java.util.List;

/**
 * Created by Vlad on 10.09.2018.
 */
public class DirectHunterFiller extends DirectFiller {

    public DirectHunterFiller(ClearMap clearMap, WeightPolicy policy, List<Position> bots, Position hero) {
        super(clearMap, policy, bots, hero);
    }

    @Override
    protected List<PositionAndCommand> produceDelayedCommands(Position position, ClearMap map) {
        return Collections.emptyList();
    }


}
