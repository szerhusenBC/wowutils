package org.zerhusen.wow.tsm.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.zerhusen.wow.tsm.cache.WowItemCache;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by stephan on 02.10.16.
 */
public class TSMCoreService {

    private static final Logger LOGGER = LogManager.getLogger(TSMCoreService.class);

    private static final String CSS_SELECTOR_GOLD = "table.htmltable .cur_g";
    private static final String CSS_SELECTOR_SPD_SERVER = "table.htmltable tr:nth-child(6) td:nth-child(2)";
    private static final String CSS_SELECTOR_SPD_REGION = "table.htmltable tr:nth-child(6) td:nth-child(3)";
    private static final String CSS_SELECTOR_NAME = ".ItemBare";

    private static final String WOW_AUCTION_URL = "http://www.wowuction.com/eu/azshara/horde/Items/Stats/";

    public List<WowItem> requestWowItems(List<Long> itemIds) {
        long start = System.currentTimeMillis();

        List<WowItem> wowItems = itemIds.stream()
                .map(this::requestWowItemFromWowAuctionPage)
                .collect(Collectors.toList());

        long duration = System.currentTimeMillis() - start;
        LOGGER.info("requested " + wowItems.size() + " items in " + duration + "ms");

        return wowItems;
    }

    public WowItem requestWowItemFromWowAuctionPage(long itemId) {
        try {
            LOGGER.info("TSMCoreService.requestPriceFromSite - requesting itemId " + itemId);
            long start = System.currentTimeMillis();
            Document doc = Jsoup.connect(WOW_AUCTION_URL + itemId).get();
            long stop = System.currentTimeMillis();
            LOGGER.info("TSMCoreService.requestPriceFromSite - requested itemId " + itemId + " in " + (stop - start) + "ms");

            String itemName = getItemNameFromSite(doc).replace(",", " ");
            Integer medianMarketPrice = getPriceFromSite(doc);
            if (medianMarketPrice == null) {
                return new WowItem(itemId, itemName);
            } else {
                double estimatedSoldPerDay = getEstimatedSoldPerDayFromSite(doc);
                return new WowItem(itemId, itemName, medianMarketPrice, estimatedSoldPerDay);
            }
        } catch (IOException e) {
            LOGGER.info("error on requesting price for item " + itemId + ", reason: " + e.getMessage());
        }

        return new WowItem(itemId, "error on requesting item");
    }

    private double getEstimatedSoldPerDayFromSite(Document doc) {
        Elements serverElements = doc.select(CSS_SELECTOR_SPD_SERVER);
        String text = serverElements.get(0).text();
        if (text == null || "".equals(text)) {
            Elements regionElements = doc.select(CSS_SELECTOR_SPD_REGION);
            text = regionElements.get(0).text();
        }
        return Double.parseDouble(text);
    }

    private String getItemNameFromSite(Document doc) {
        Elements nameElements = doc.select(CSS_SELECTOR_NAME);
        return nameElements.get(0).text();
    }

    private Integer getPriceFromSite(Document doc) {
        Elements myServerMaketValueElements = doc.select(CSS_SELECTOR_GOLD);
        if (myServerMaketValueElements.isEmpty()) {
            return null;
        } else {
            Element myServerMaketValueElement = myServerMaketValueElements.get(0);

            String marketPriceTextFromNode = myServerMaketValueElement.text();
            return Integer.valueOf(marketPriceTextFromNode);
        }
    }

    public Map<TSMPriceCategory, List<WowItem>> getCategoryPriceMap(List<WowItem> wowItems) {
        final Map<TSMPriceCategory, List<WowItem>> priceCategoryMap = new HashMap<>();

        wowItems.forEach(wowItem -> {
            Integer marketPrice = wowItem.getMedianMarketPrice();
            if (marketPrice == null) {
                addItemToMap(priceCategoryMap, wowItem, TSMPriceCategory.CATEGORY_DIVERSE);
            } else {
                TSMPriceCategory priceCategory = Arrays.asList(TSMPriceCategory.values()).stream()
                        .filter(category -> category.liesInCategory(marketPrice))
                        .findAny()
                        .get();
                addItemToMap(priceCategoryMap, wowItem, priceCategory);
            }
        });

        return priceCategoryMap;
    }

    private void addItemToMap(Map<TSMPriceCategory, List<WowItem>> priceCategoryMap, WowItem wowItem, TSMPriceCategory priceCategory) {
        if (priceCategory == null) {
            LOGGER.info("couldn't find price category for itemId " + wowItem + ", price: " + wowItem.getMedianMarketPrice());
        } else {
            if (!priceCategoryMap.containsKey(priceCategory)) {
                priceCategoryMap.put(priceCategory, new ArrayList<>());
            }
            priceCategoryMap.get(priceCategory).add(wowItem);
        }
    }
}
