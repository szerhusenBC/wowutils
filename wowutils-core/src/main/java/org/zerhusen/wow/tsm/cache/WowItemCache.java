package org.zerhusen.wow.tsm.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zerhusen.wow.tsm.service.TSMCoreService;
import org.zerhusen.wow.tsm.service.WowItem;
import org.zerhusen.wow.tsm.service.WowItemCached;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by stephan on 26.11.16.
 */
public class WowItemCache {

    private static final Logger LOGGER = LogManager.getLogger(WowItemCache.class);

    private static final TSMCoreService TSM_CORE_SERVICE = new TSMCoreService();

    private Map<Long, WowItemCached> cacheMap = new HashMap<>();

    public void loadCacheFromFile(String rootPath) {
        File cacheFile = new File(rootPath + "/items.cache");
        if (cacheFile.exists()) {
            LOGGER.info("parsing cache file '{}'", cacheFile.getAbsolutePath());
            final ObjectMapper mapper = getMapper();
            TypeFactory typeFactory = mapper.getTypeFactory();
            MapType mapType = typeFactory.constructMapType(HashMap.class, Long.class, WowItemCached.class);

            try {
                cacheMap = mapper.readValue(cacheFile, mapType);
                LOGGER.info("cache file parsed, found {} entries", cacheMap.size());
            } catch (IOException e) {
                LOGGER.error("couldn't find cache file", e);
            }
        } else {
            LOGGER.info("couldn't find cache file '{}'", cacheFile.getAbsolutePath());
        }
    }

    public void storeCacheInFile(String rootPath) {
        final ObjectMapper mapper = getMapper();
        try {
            File cacheFile = new File(rootPath + "/items.cache");
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(cacheFile, cacheMap);
            LOGGER.info("wrote cache file '{}'", cacheFile);
        } catch (IOException e) {
            LOGGER.error("couldn't write cache file", e);
        }
    }


    public WowItem getWowItem(long itemId) {
        WowItemCached itemFromCache = cacheMap.get(itemId);
        if (itemFromCache != null) {
            LOGGER.info("found item '{}' in cache", itemId);
            return itemFromCache.getWowItem();
        } else {
            LOGGER.info("couldn't find item '{}' in cache, requesting it", itemId);
            WowItem wowItem = TSM_CORE_SERVICE.requestWowItemFromWowAuctionPage(itemId);

            if (wowItem != null && wowItem.getMedianMarketPrice() != null) {
                LOGGER.info("item '{}' has price information, putting it in cache", itemId);
                cacheMap.put(itemId, new WowItemCached(wowItem, LocalDateTime.now()));
            }

            return wowItem;
        }
    }

    private ObjectMapper getMapper() {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
        return mapper;
    }
}
