package org.zerhusen.wow.tsm.service;

import java.util.Objects;

/**
 * Created by stephan on 19.11.16.
 */
public class WowItem {
    private final long itemId;
    private final String name;
    private Integer medianMarketPrice;
    private Double estimatedSoldPerDay;

    public WowItem(long itemId, String name) {
        this.itemId = itemId;
        this.name = name;
    }

    public WowItem(long itemId, String name, int medianMarketPrice, double estimatedSoldPerDay) {
        this.itemId = itemId;
        this.name = name;
        this.medianMarketPrice = medianMarketPrice;
        this.estimatedSoldPerDay = estimatedSoldPerDay;
    }

    public long getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public Integer getMedianMarketPrice() {
        return medianMarketPrice;
    }

    public Double getEstimatedSoldPerDay() {
        return estimatedSoldPerDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WowItem wowItem = (WowItem) o;
        return itemId == wowItem.itemId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId);
    }

    @Override
    public String toString() {
        return "WowItem{" +
                "itemId=" + itemId +
                ", name='" + name + '\'' +
                '}';
    }
}
