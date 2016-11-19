package org.zerhusen.wow.tsm.service;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

/**
 * Created by stephan on 02.10.16.
 */
public class TSMCoreServiceTest {
    @Test
    public void getCategoryPriceMap() throws Exception {
        TSMCoreService tsmCoreService = new TSMCoreService();

        List<WowItem> wowItems = new ArrayList<>();
        WowItem item321 = new WowItem(321L, "item321", 265, 0.1D);
        wowItems.add(item321);
        WowItem item2453 = new WowItem(2453L, "item2453", 2065, 0.1D);
        wowItems.add(item2453);
        WowItem item2133 = new WowItem(2133L, "item2133", 2545, 0.1D);
        wowItems.add(item2133);
        WowItem item154811 = new WowItem(154811L, "item154811", 150320, 0.1D);
        wowItems.add(item154811);
        WowItem item49 = new WowItem(49L, "item49", 20, 0.1D);
        wowItems.add(item49);

        Map<TSMPriceCategory, List<WowItem>> categoryPriceMap = tsmCoreService.getCategoryPriceMap(wowItems);

        assertThat(categoryPriceMap.keySet())
                .containsOnly(
                        TSMPriceCategory.CATEGORY_0_50,
                        TSMPriceCategory.CATEGORY_250_500,
                        TSMPriceCategory.CATEGORY_2000_3000,
                        TSMPriceCategory.CATEGORY_150000_250000
                );

        assertThat(categoryPriceMap.get(TSMPriceCategory.CATEGORY_0_50)).containsOnly(item49);
        assertThat(categoryPriceMap.get(TSMPriceCategory.CATEGORY_250_500)).containsOnly(item321);
        assertThat(categoryPriceMap.get(TSMPriceCategory.CATEGORY_2000_3000)).containsOnly(item2453, item2133);
        assertThat(categoryPriceMap.get(TSMPriceCategory.CATEGORY_150000_250000)).containsOnly(item154811);
    }

}