package org.silentpom.runner.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Created by Vlad on 04.10.2018.
 */
public class PropertiesUtil {

    public static Logger LOGGER = LoggerFactory.getLogger(PropertiesUtil.class);

    public static int getValue(Properties props, String name, int defValue) {
        String value = props.getProperty(name, "<none>");

        try {
            int result = Integer.parseInt(value);
            LOGGER.info("Configuration parameter {} is {}", name, result);
            return result;
        } catch (NumberFormatException ex) {
            LOGGER.info("Configuration parameter {} is incorrect {}, use {}", name, value, defValue);
        }
        return defValue;
    }
}
