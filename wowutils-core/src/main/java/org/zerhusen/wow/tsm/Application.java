package org.zerhusen.wow.tsm;

import org.zerhusen.wow.tsm.service.TSMCoreService;
import org.zerhusen.wow.tsm.service.TSMPriceCategory;
import org.zerhusen.wow.tsmparser.TSMParserUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by stephan on 02.10.16.
 */
public class Application {
    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            System.out.println("please specify path to file with TSM import string!");
        } else {
            String tsmString = readStringFromFile(args[0]);

            if (tsmString == null || "".equals(tsmString)) {
                System.out.println("couldn't find TSM string in first line of file!");
            } else {
                List<Long> itemIds = TSMParserUtils.extractItemIds(tsmString);

                TSMCoreService tsmCoreService = new TSMCoreService();
                Map<TSMPriceCategory, List<Long>> categoryPriceMap = tsmCoreService.getCategoryPriceMap(itemIds);

                printOutPriceStrings(categoryPriceMap);
            }
        }
    }

    private static String readStringFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // just read first line
            return br.readLine();

        } catch (IOException e) {
            System.err.println("couldn't read file: " + e.getMessage());
        }

        return null;
    }

    private static void printOutPriceStrings(Map<TSMPriceCategory, List<Long>> categoryPriceMap) {
        categoryPriceMap.entrySet().forEach(entry -> {
            System.out.println(entry.getKey().name());
            System.out.println("==============================================================");

            String tsmImportString = entry.getValue().stream()
                    .map(itemId -> "i:" + itemId)
                    .collect(Collectors.joining(","));

            System.out.println(tsmImportString);
        });
    }
}
