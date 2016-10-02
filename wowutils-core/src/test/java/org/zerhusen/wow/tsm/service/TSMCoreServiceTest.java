package org.zerhusen.wow.tsm.service;

import org.junit.Test;
import org.mockito.Mockito;

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
    public void requestMarketPrice() throws Exception {
        TSMCoreService tsmCoreService = new TSMCoreService();

        assertThat(tsmCoreService.requestMarketPrice(93501L)).isNotNull();
        assertThat(tsmCoreService.requestMarketPrice(99999999L)).isNull();
    }

    @Test
    public void getCategoryPriceMap() throws Exception {
        TSMCoreService tsmCoreService = Mockito.spy(TSMCoreService.class);

        doReturn(321).when(tsmCoreService).requestMarketPrice(321L);
        doReturn(2453).when(tsmCoreService).requestMarketPrice(2453L);
        doReturn(2133).when(tsmCoreService).requestMarketPrice(2133L);
        doReturn(154811).when(tsmCoreService).requestMarketPrice(154811L);
        doReturn(49).when(tsmCoreService).requestMarketPrice(49L);

        Map<TSMPriceCategory, List<Long>> categoryPriceMap = tsmCoreService.getCategoryPriceMap(Arrays.asList(321L, 2453L, 2133L, 154811L, 49L));

        assertThat(categoryPriceMap.keySet())
                .containsOnly(
                        TSMPriceCategory.CATEGORY_0_50,
                        TSMPriceCategory.CATEGORY_250_500,
                        TSMPriceCategory.CATEGORY_2000_3000,
                        TSMPriceCategory.CATEGORY_150000_250000
                );

        assertThat(categoryPriceMap.get(TSMPriceCategory.CATEGORY_0_50)).containsOnly(49L);
        assertThat(categoryPriceMap.get(TSMPriceCategory.CATEGORY_250_500)).containsOnly(321L);
        assertThat(categoryPriceMap.get(TSMPriceCategory.CATEGORY_2000_3000)).containsOnly(2453L, 2133L);
        assertThat(categoryPriceMap.get(TSMPriceCategory.CATEGORY_150000_250000)).containsOnly(154811L);
    }

}