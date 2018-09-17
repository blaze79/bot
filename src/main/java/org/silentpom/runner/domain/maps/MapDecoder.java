package org.silentpom.runner.domain.maps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Vlad on 17.09.2018.
 */
public class MapDecoder {
    public static Logger LOGGER = LoggerFactory.getLogger(MapDecoder.class);

    String boardPatternStr = "^board=(.*)$";
    Pattern boardPattern = Pattern.compile(boardPatternStr);

    public FullMapInfo mapDecode(String map) {
        LOGGER.info(map);

        Matcher matcher = boardPattern.matcher(map);
        if (!matcher.matches()) {
            LOGGER.error("PATTERN ERROR {}", map);
            throw new RuntimeException("PATTERN ERROR");
        }

        SimpleMap simpleMap = SimpleMap.fromLongString(matcher.group(1));
        LOGGER.info("Map:\n {}", simpleMap.getStringView());

        return FullMapInfo.buildFromMap(simpleMap);
    }
}
