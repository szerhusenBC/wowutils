package org.zerhusen.wow.tsm.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by stephan on 26.11.16.
 */
public class WowItemCached implements Serializable {

    private WowItem wowItem;
    private LocalDateTime requestDate;

    @JsonCreator
    public WowItemCached(
            @JsonProperty("wowItem") WowItem wowItem,
            @JsonProperty("requestDate") LocalDateTime requestDate
    ) {
        this.wowItem = wowItem;
        this.requestDate = requestDate;
    }

    public WowItem getWowItem() {
        return wowItem;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }
}
