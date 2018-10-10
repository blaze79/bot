package org.silentpom.runner.algo.estimation;

import org.silentpom.runner.algo.estimation.policy.WeightPolicy;
import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.maps.ClearMap;
import org.silentpom.runner.domain.masks.BitMask;
import org.silentpom.runner.domain.masks.DoubleMask;
import org.silentpom.runner.domain.state.PositionAndCommand;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vlad on 10.09.2018.
 */
public abstract class AbstractFiller {
    BitMask mask;
    DoubleMask result;
    WeightPolicy policy;
    List<FillerState> currentBorder = new ArrayList<>(100);
    List<FillerState> newGeneration = new ArrayList<>(100);
    ClearMap clearMap;
    FillerResultHolder holder;

    protected AbstractFiller(ClearMap clearMap, WeightPolicy policy, List<Position> bots, Position hero) {
        mask = new BitMask(clearMap.rows(), clearMap.columns());
        result = new DoubleMask(clearMap.rows(), clearMap.columns());
        this.clearMap = clearMap;
        this.policy = policy;
        holder = new FillerResultHolder(bots, hero, mask, result);
    }

    /*public FillerResultHolder estimation(Position start) {
        currentBorder.clear();
        newGeneration.clear();

        FillerState init = new FillerState(start, policy.startWeight());
        mark(init);
        swapBorders();

        while (!currentBorder.isEmpty()) {
            for (FillerState state : currentBorder) {
                processState(state);
            }
            swapBorders();
        }
        return holder;
    }*/

    public FillerResultHolder estimation(Position start) {
        return estimation(start, 10000);
    }

    public FillerResultHolder estimation(Position start, int limit) {
        currentBorder.clear();
        newGeneration.clear();
        holder.setStartPosition(start);

        FillerState init = new FillerState(start, policy.startWeight(start));
        mark(init);
        swapBorders();

        int iter = 0;

        while (!currentBorder.isEmpty()) {
            for (FillerState state : currentBorder) {
                processState(state);
            }
            swapBorders();

            if (++iter >= limit) {
                break;
            }
        }
        return holder;
    }

    private void processState(FillerState state) {
        /*List<Position> previousPoints = MoveCommandsSet.MOVE_COMMANDS.getPreviousPoints(
                state.getPosition(),
                clearMap
        );*/

        if (!state.isDelayed()) {
            List<PositionAndCommand> previousPoints = produceActiveCommands(
                    state.getPosition(),
                    clearMap
            );

            for (PositionAndCommand pair : previousPoints) {
                Position pos = pair.getPosition();
                if (!mask.getChecked(pos)) {
                    FillerState child = state.makeChildren(
                            pos,
                            pair.getCommand(),
                            policy.reduceWeight(
                                    state.getValue(),
                                    pair.getCommand().tickCount()
                            )
                    );
                    mark(child);
                }
            }

            List<PositionAndCommand> digPoints = produceDelayedCommands(
                    state.getPosition(),
                    clearMap
            );

            for (PositionAndCommand pair : digPoints) {
                Position pos = pair.getPosition();
                if (!mask.getChecked(pos)) {
                    FillerState child = state.makeDelayChildren(
                            pos,
                            pair.getCommand(),
                            policy.reduceWeight(
                                    state.getValue(),
                                    pair.getCommand().tickCount()
                            )
                    );
                    pushDelayed(child);
                }
            }
        } else {
            processDelayedState(state);
        }

    }

    protected abstract List<PositionAndCommand> produceActiveCommands(Position position, ClearMap map );

    protected abstract List<PositionAndCommand> produceDelayedCommands(Position position, ClearMap map );


    private void processDelayedState(FillerState state) {
        if(state.makeDelayedStep()) {
            if (!mask.getChecked(state.getPosition())) {
                mark(state);
            }
        } else {
            pushDelayed(state);
        }
    }

    private void mark(FillerState state) {
        newGeneration.add(state);
        //mask.setChecked(state.getPosition(), true);
        holder.markCell(state.getPosition());
        result.setValue(state.getPosition(), state.getValue());

        holder.checkOrder(state);
    }

    private void pushDelayed(FillerState state) {
        newGeneration.add(state);
    }

    private void swapBorders() {

//        System.out.printf("Active border: %d New generation %d of them delayed %d \n",
//                currentBorder.size(),
//                newGeneration.size(),
//                newGeneration.stream().filter( x -> x.isDelayed()).count()
//        );

        List<FillerState> temp = currentBorder;
        currentBorder = newGeneration;
        newGeneration = temp;

        newGeneration.clear();
    }

}
