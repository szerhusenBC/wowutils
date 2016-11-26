package org.zerhusen.wow.tsm.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by stephan on 19.11.16.
 */
public class WowItem implements Serializable {

    private final long itemId;
    private final String name;
    private Integer medianMarketPrice;
    private Double estimatedSoldPerDay;

    public WowItem(long itemId, String name) {
        this.itemId = itemId;
        this.name = name;
    }

    @JsonCreator
    public WowItem(
            @JsonProperty("itemId") long itemId,
            @JsonProperty("name") String name,
            @JsonProperty("medianMarketPrice") int medianMarketPrice,
            @JsonProperty("estimatedSoldPerDay") double estimatedSoldPerDay
    ) {
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
