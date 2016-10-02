package org.zerhusen.wow.tsm.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

/**
 * Created by stephan on 02.10.16.
 */
public class TSMCoreService {

    private static final String CSS_ELEMENT_SELECTOR = "table.table.table-condensed.table-hover tr:nth-of-type(2) td:nth-of-type(2)";

    public Map<TSMPriceCategory, List<Long>> getCategoryPriceMap(List<Long> itemIds) {
        final Map<TSMPriceCategory, List<Long>> priceCategoryMap = Collections.synchronizedMap(new HashMap<>());

        itemIds.parallelStream()
                .forEach(itemId -> {
                    Integer marketPrice = requestMarketPrice(itemId);
                    if (marketPrice == null) {
                        System.out.println("couldn't request price for itemId " + itemId);
                    } else {
                        TSMPriceCategory priceCategory = Arrays.asList(TSMPriceCategory.values()).stream()
                                .filter(category -> category.liesInCategory(marketPrice))
                                .findAny()
                                .orElse(null);
                        addItemIdToMap(priceCategoryMap, itemId, marketPrice, priceCategory);
                    }
                });

        return priceCategoryMap;
    }

    private void addItemIdToMap(Map<TSMPriceCategory, List<Long>> priceCategoryMap, Long itemId, Integer marketPrice, TSMPriceCategory priceCategory) {
        if (priceCategory == null) {
            System.out.println("couldn't find price category for itemId " + itemId + ", price: " + marketPrice);
        } else {
            if (!priceCategoryMap.containsKey(priceCategory)) {
                priceCategoryMap.put(priceCategory, new ArrayList<>());
            }
            priceCategoryMap.get(priceCategory).add(itemId);
        }
    }

    public Integer requestMarketPrice(long itemId) {

        try {
            String goldPriceAsString = requestPriceFromSite(itemId);
            return Integer.valueOf(goldPriceAsString);
        } catch (IOException e) {
            //do nothing
        }

        return null;
    }

    String requestPriceFromSite(long itemId) throws IOException {
        System.out.println("TSMCoreService.requestPriceFromSite - requesting itemId " + itemId);
        long start = System.currentTimeMillis();
        Document doc = Jsoup.connect("https://www.tradeskillmaster.com/items/" + itemId).get();
        long stop = System.currentTimeMillis();
        System.out.println("TSMCoreService.requestPriceFromSite - requested itemId " + itemId + " in " + (stop - start) + "ms");

        Elements myServerMaketValueElements = doc.select(CSS_ELEMENT_SELECTOR);
        Element myServerMaketValueElement = myServerMaketValueElements.get(0);

        String marketPriceTextFromNode = myServerMaketValueElement.text();
        return marketPriceTextFromNode.substring(0, marketPriceTextFromNode.indexOf('g')).replace(",", "");
    }
}