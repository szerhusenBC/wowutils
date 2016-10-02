package org.zerhusen.wow.tsm.service;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by stephan on 02.10.16.
 */
public class TSMPriceCategoryTest {
    @Test
    public void liesInCategory() throws Exception {
        assertThat(TSMPriceCategory.CATEGORY_0_50.liesInCategory(123)).isFalse();
        assertThat(TSMPriceCategory.CATEGORY_0_50.liesInCategory(45)).isTrue();
        assertThat(TSMPriceCategory.CATEGORY_50_100.liesInCategory(50)).isTrue();
        assertThat(TSMPriceCategory.CATEGORY_50_100.liesInCategory(100)).isFalse();
    }

}