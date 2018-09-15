package org.silentpom.runner.algo.estimation;

import org.silentpom.runner.algo.estimation.policy.LinearWeightPolicy;
import org.silentpom.runner.algo.estimation.policy.WeightPolicy;
import org.silentpom.runner.domain.CellCategory;
import org.silentpom.runner.domain.CellType;
import org.silentpom.runner.domain.Constants;
import org.silentpom.runner.domain.Position;
import org.silentpom.runner.domain.maps.ClearMap;
import org.silentpom.runner.domain.maps.FullMapInfo;
import org.silentpom.runner.domain.masks.BitMask;
import org.silentpom.runner.domain.masks.DoubleMask;
import org.silentpom.runner.domain.state.PositionsCache;

import java.util.Collections;

import static org.silentpom.runner.domain.Constants.GOLD_COST;
import static org.silentpom.runner.domain.Constants.RATE_INFLATION;

/**
 * Created by Vlad on 15.09.2018.
 */
public class DeadHoleSearcher {
    public BitMask findDeadHoles(FullMapInfo position, int maxDeadArea) {
        ClearMap map = position.getClearMap();
        BitMask result = new BitMask(map.rows(), map.columns());

        for (int row = 0; row < map.rows(); ++row) {
            for (int column = 0; column < map.columns(); ++column) {
                Position pos = PositionsCache.make(row, column);
                result.setChecked(pos, false);

                CellType cell = map.getCell(pos);

                if (cell.getCategory() == CellCategory.WALL) {
                    continue;
                }

                DirectFiller filler = new DirectFiller(
                        map,
                        LOCAL_POLICY,
                        Collections.emptyList(),
                        pos
                );

                FillerResultHolder estimation = filler.estimation(pos, maxDeadArea + 5);
                if (estimation.getMarks() < maxDeadArea) {
                    result.setChecked(pos, true);
                }
            }
        }

        lastSavedResult = result;
        return result;
    }

    public static DoubleMask getInitialEstimationValue(int rows, int columns) {
        DoubleMask doubleMask = new DoubleMask(rows, columns);
        if (lastSavedResult == null) {
            return doubleMask;
        }

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                if(lastSavedResult.getChecked(i, j)) {
                    doubleMask.setValue(i, j, Constants.DEADEND_COST);
                }
            }
        }
        return doubleMask;
    }

    public static void clear() {
        lastSavedResult = null;
    }
    
    private static BitMask lastSavedResult = null;
    private static WeightPolicy LOCAL_POLICY = new LinearWeightPolicy(GOLD_COST, RATE_INFLATION);
}
