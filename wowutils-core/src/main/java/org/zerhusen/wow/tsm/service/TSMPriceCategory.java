package org.zerhusen.wow.tsm.service;

/**
 * Created by stephan on 02.10.16.
 */
public enum TSMPriceCategory {

    CATEGORY_0_50(0, 50),
    CATEGORY_50_100(50, 100),
    CATEGORY_100_250(100, 250),
    CATEGORY_250_500(250, 500),
    CATEGORY_500_775(500, 775),
    CATEGORY_775_1000(775, 1000),
    CATEGORY_1000_2000(1000, 2000),
    CATEGORY_2000_3000(2000, 3000),
    CATEGORY_3000_4000(3000, 4000),
    CATEGORY_4000_5000(4000, 5000),
    CATEGORY_5000_7500(5000, 7500),
    CATEGORY_7500_10000(7500, 10000),
    CATEGORY_10000_15000(10000, 15000),
    CATEGORY_15000_20000(15000, 20000),
    CATEGORY_20000_30000(20000, 30000),
    CATEGORY_30000_40000(30000, 40000),
    CATEGORY_40000_50000(40000, 50000),
    CATEGORY_50000_75000(50000, 75000),
    CATEGORY_75000_100000(75000, 100000),
    CATEGORY_100000_150000(100000, 150000),
    CATEGORY_150000_250000(150000, 250000),
    CATEGORY_250000_500000(250000, 500000),
    CATEGORY_500000_1000000(500000, 1000000);

    private int minPrice;
    private int maxPrice;

    TSMPriceCategory(int minPrice, int maxPrice) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public boolean liesInCategory(int price) {
        return price >= minPrice && price < maxPrice;
    }
}
