package org.zerhusen.wow.tsm.renderer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zerhusen.wow.tsm.Application;
import org.zerhusen.wow.tsm.service.TSMPriceCategory;
import org.zerhusen.wow.tsm.service.WowItem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by stephan on 26.11.16.
 */
public class ItemListCSVFileRenderer implements FileRenderer {

    private static final Logger LOGGER = LogManager.getLogger(ItemListCSVFileRenderer.class);

    private static final String SEPERATOR = ",";

    @Override
    public void render(String rootPath, Map<TSMPriceCategory, List<WowItem>> categoryPriceMap) {
        File csvFile = new File(rootPath + "/wowItemPrices.csv");

        try (
                FileWriter fw = new FileWriter(csvFile.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw)
        ) {
            bw.write(getCsvContent(categoryPriceMap));
            LOGGER.info("wrote CSV list file to '{}'", csvFile.getAbsolutePath());
        } catch (IOException e) {
            LOGGER.error("couldn't write item list file " + e);
        }
    }

    private static String getCsvContent(Map<TSMPriceCategory, List<WowItem>> categoryPriceMap) {
        StringBuffer sb = new StringBuffer();

        categoryPriceMap.entrySet().forEach(entry -> {
            final String categoryLabel = entry.getKey().getLabel();
            entry.getValue().stream()
                    .forEach(wowItem -> {
                        sb
                                .append(wowItem.getItemId()).append(SEPERATOR)
                                .append(wowItem.getName()).append(SEPERATOR);

                        if (wowItem.getMedianMarketPrice() != null) {
                            sb.append(wowItem.getMedianMarketPrice());
                        }
                        sb.append(SEPERATOR);

                        if (wowItem.getEstimatedSoldPerDay() != null) {
                            sb.append(wowItem.getEstimatedSoldPerDay());
                        }
                        sb.append(SEPERATOR);

                        sb.append(categoryLabel).append("\n");
                    });
        });

        return sb.toString();
    }
}
