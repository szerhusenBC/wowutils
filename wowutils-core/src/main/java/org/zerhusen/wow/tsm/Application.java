package org.zerhusen.wow.tsm;

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

    private static final String SEPERATOR = ",";

    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            System.out.println("please specify path to file with TSM import string!");
        } else {
            String tsmString = readStringFromFile(args[0]);

            if (tsmString == null || "".equals(tsmString)) {
                System.out.println("couldn't find TSM string in first line of file!");
            } else {
                List<Long> itemIds = SimpleTsmParser.extractItemIds(tsmString);

                TSMCoreService tsmCoreService = new TSMCoreService();
                List<WowItem> wowItems = tsmCoreService.getWowItems(itemIds);
                Map<TSMPriceCategory, List<WowItem>> categoryPriceMap = tsmCoreService.getCategoryPriceMap(wowItems);

                final String rootPath = initRootPath();
                try {
                    generateItemListCSVFile(rootPath, categoryPriceMap);
                } catch (IOException e) {
                    System.out.println("couldn't write item list file, reason: " + e.getMessage());
                }

                try {
                    generateTsmImportStringFile(rootPath, categoryPriceMap);
                } catch (IOException e) {
                    System.out.println("couldn't write TSM import string file, reason: " + e.getMessage());
                }
            }
        }
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
            System.err.println("couldn't read file: " + e.getMessage());
        }

        return null;
    }

    private static void generateItemListCSVFile(String rootPath, Map<TSMPriceCategory, List<WowItem>> categoryPriceMap) throws IOException {
        File csvFile = new File(rootPath + "/wowItemPrices.csv");
        if (csvFile.exists()) {
            csvFile.delete();
        }

        FileWriter fw = new FileWriter(csvFile.getAbsoluteFile());
        try (BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(getCsvContent(categoryPriceMap));
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

    private static void generateTsmImportStringFile(String rootPath, Map<TSMPriceCategory, List<WowItem>> categoryPriceMap) throws IOException {
        File importStringFile = new File(rootPath + "/wowTSMImportString.txt");
        if (importStringFile.exists()) {
            importStringFile.delete();
        }

        FileWriter fw = new FileWriter(importStringFile.getAbsoluteFile());
        try (BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(getTsmImportStringContent(categoryPriceMap));
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
