package org.zerhusen.wow.tsmparser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by stephan on 19.11.16.
 */
public final class SimpleTsmParser {

    private static final Pattern PATTERN = Pattern.compile("i:(\\d+)");

    private SimpleTsmParser() {
    }

    public static List<Long> extractItemIds(String tsmString) {
        List<Long> itemIds = new ArrayList<>();
        Matcher matcher = PATTERN.matcher(tsmString);

        while (matcher.find()) {
            itemIds.add(Long.valueOf(matcher.group(1)));
        }

        return itemIds;
    }
}
