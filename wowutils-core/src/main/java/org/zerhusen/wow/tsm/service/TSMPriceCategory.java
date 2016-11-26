package org.zerhusen.wow.tsm.service;

/**
 * Created by stephan on 02.10.16.
 */
public enum TSMPriceCategory {

    CATEGORY_0_50("A - 0g-50g", 0, 50),
    CATEGORY_50_100("B - 50g-100g", 50, 100),
    CATEGORY_100_250("C - 1000g-250g", 100, 250),
    CATEGORY_250_500("D - 250g-500g", 250, 500),
    CATEGORY_500_775("E - 500g-775g", 500, 775),
    CATEGORY_775_1000("F - 775g-1000g", 775, 1000),
    CATEGORY_1000_2000("G - 1000g-2000g", 1000, 2000),
    CATEGORY_2000_3000("H - 2000g-3000g", 2000, 3000),
    CATEGORY_3000_4000("I - 3000g-4000g", 3000, 4000),
    CATEGORY_4000_5000("J - 4000g-5000g", 4000, 5000),
    CATEGORY_5000_7500("K - 5k-7.5k", 5000, 7500),
    CATEGORY_7500_10000("L - 7.5k-10k", 7500, 10000),
    CATEGORY_10000_15000("M - 10k-15k", 10000, 15000),
    CATEGORY_15000_20000("N - 15k-20k", 15000, 20000),
    CATEGORY_20000_30000("O - 20k-30k", 20000, 30000),
    CATEGORY_30000_40000("P - 30k-40k", 30000, 40000),
    CATEGORY_40000_50000("Q - 40k-50k", 40000, 50000),
    CATEGORY_50000_75000("R - 50k-75k", 50000, 75000),
    CATEGORY_75000_100000("S - 75k-100k", 75000, 100000),
    CATEGORY_100000_150000("T - 100k-150k", 100000, 150000),
    CATEGORY_150000_250000("U - 150k-250k", 150000, 250000),
    CATEGORY_250000_500000("V - 250k-500k", 250000, 500000),
    CATEGORY_500000_1000000("W - 500k-1000k", 500000, 1000000),
    CATEGORY_DIVERSE("X - diverse", 1000000, Integer.MAX_VALUE);

    private String label;
    private int minPrice;
    private int maxPrice;

    TSMPriceCategory(String label, int minPrice, int maxPrice) {
        this.label = label;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public String getLabel() {
        return label;
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
