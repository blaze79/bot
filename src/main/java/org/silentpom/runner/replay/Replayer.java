package org.silentpom.runner.replay;

import org.silentpom.runner.algo.estimation.FillerState;
import org.silentpom.runner.domain.CellType;
import org.silentpom.runner.domain.maps.ClearMap;
import org.silentpom.runner.domain.maps.SimpleMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Vlad on 12.09.2018.
 */
public class Replayer {
    public static Logger LOGGER = LoggerFactory.getLogger(Replayer.class);

    public void replay(ClearMap map, List<FillerState> states) {
        LOGGER.info("Lets the show begin");
        for (FillerState state : states) {
            SimpleMap stepMap = SimpleMap.fromClearMap(map);
            stepMap.setCell(
                    state.getPosition().getRow(),
                    state.getPosition().getColumn(),
                    CellType.HERO_LEFT
            );
            LOGGER.info(stepMap.getStringView());
            if (state.getCommand() != null) {
                waitCommand(state.getCommand().tickCount());
            }
        }
        LOGGER.info("End of show");
    }

    private void waitCommand(int i) {
        try {
            Thread.sleep(i*1000);
        } catch (InterruptedException e) {

        }
    }
}
