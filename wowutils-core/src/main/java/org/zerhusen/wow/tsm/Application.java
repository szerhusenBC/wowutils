package org.zerhusen.wow.tsm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zerhusen.wow.tsm.cache.WowItemCache;
import org.zerhusen.wow.tsm.renderer.ItemListCSVFileRenderer;
import org.zerhusen.wow.tsm.renderer.TSMImportStringFileRenderer;
import org.zerhusen.wow.tsm.service.TSMCoreService;
import org.zerhusen.wow.tsm.service.TSMPriceCategory;
import org.zerhusen.wow.tsm.service.WowItem;
import org.zerhusen.wow.tsmparser.SimpleTsmParser;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by stephan on 02.10.16.
 */
public class Application {

    private static final Logger LOGGER = LogManager.getLogger(Application.class);

    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            LOGGER.info("please specify path to file with TSM import string!");
        } else {
            String tsmString = readStringFromFile(args[0]);

            if (tsmString == null || "".equals(tsmString)) {
                LOGGER.info("couldn't find TSM string in first line of file!");
            } else {
                List<Long> itemIds = SimpleTsmParser.extractItemIds(tsmString);

                final String rootPath = initRootPath();

                List<WowItem> wowItems = getWowItems(itemIds, rootPath);

                Map<TSMPriceCategory, List<WowItem>> categoryPriceMap = new TSMCoreService().getCategoryPriceMap(wowItems);
                new TSMImportStringFileRenderer().render(rootPath, categoryPriceMap);
                new ItemListCSVFileRenderer().render(rootPath, categoryPriceMap);
            }
        }
    }

    private static List<WowItem> getWowItems(List<Long> itemIds, String rootPath) {
        final WowItemCache itemCache = new WowItemCache();
        itemCache.loadCacheFromFile(rootPath);

        List<WowItem> wowItems = itemIds.stream()
                .map(itemId -> itemCache.getWowItem(itemId))
                .collect(Collectors.toList());

        itemCache.storeCacheInFile(rootPath);
        return wowItems;
    }

    private static String initRootPath() {
        String rootPath = System.getProperty("user.home") + "/wowutils";
        File rootDirectory = new File(rootPath);
        if (!rootDirectory.exists()) {
            rootDirectory.mkdir();
        }
        return rootPath;
    }

    private static String readStringFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // just read first line
            return br.readLine();

        } catch (IOException e) {
            LOGGER.error("couldn't read file ", e);
        }

        return null;
    }
}
