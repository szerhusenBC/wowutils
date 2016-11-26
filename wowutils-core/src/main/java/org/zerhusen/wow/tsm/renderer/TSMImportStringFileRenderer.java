package org.zerhusen.wow.tsm.renderer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zerhusen.wow.tsm.service.TSMPriceCategory;
import org.zerhusen.wow.tsm.service.WowItem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by stephan on 26.11.16.
 */
public class TSMImportStringFileRenderer implements FileRenderer {

    private static final Logger LOGGER = LogManager.getLogger(TSMImportStringFileRenderer.class);

    private static final String SEPERATOR = ",";

    @Override
    public void render(String rootPath, Map<TSMPriceCategory, List<WowItem>> categoryPriceMap) {
        File importStringFile = new File(rootPath + "/wowTSMImportString.txt");

        try (
                FileWriter fw = new FileWriter(importStringFile.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw)
        ) {
            bw.write(getTsmImportStringContent(categoryPriceMap));
            LOGGER.info("wrote TSM import string file to '{}'", importStringFile.getAbsolutePath());
        } catch (IOException e) {
            LOGGER.error("couldn't write TSM import string file" + e);
        }
    }

    private static String getTsmImportStringContent(Map<TSMPriceCategory, List<WowItem>> categoryPriceMap) {
        StringBuffer sb = new StringBuffer();

        categoryPriceMap.entrySet().forEach(entry -> {
            sb.append("group:").append(entry.getKey().getLabel()).append(SEPERATOR);

            String tsmImportString = entry.getValue().stream()
                    .map(wowItem -> "i:" + wowItem.getItemId())
                    .collect(Collectors.joining(SEPERATOR));

            sb.append(tsmImportString).append(SEPERATOR);
        });

        return sb.toString();
    }
}
