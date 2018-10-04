package org.silentpom.runner.algo.solve.prefilter;

import org.silentpom.runner.algo.estimation.Estimator;
import org.silentpom.runner.utils.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Created by Vlad on 05.10.2018.
 */
public class EstimationFilter {
    private int limit = 25;

    public static Logger LOGGER = LoggerFactory.getLogger(EstimationFilter.class);

    public boolean shouldBeRespawn(Estimator.Result estimation) {
        Integer distance = estimation.bestGoldDistance();
        if (distance == null || distance > limit) {
            LOGGER.info("There is no gold in estimation. I will be resurrected in better world");
            return true;
        }
        return false;
    }

    public void readProperties(Properties properties) {
        limit = PropertiesUtil.getValue(properties, "estimation.filter.limit", limit);
    }
}
