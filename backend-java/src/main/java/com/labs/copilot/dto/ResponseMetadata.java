package com.labs.copilot.dto;

import java.time.LocalDateTime;

/**
 * Response metadata for API responses.
 */
public class ResponseMetadata {
    private Boolean cached;
    private String cacheAge;
    private String source;
    private String searchTime;
    private String dataFreshness;
    private LocalDateTime timestamp;

    public ResponseMetadata() {
        this.timestamp = LocalDateTime.now();
    }

    public Boolean getCached() {
        return cached;
    }

    public void setCached(Boolean cached) {
        this.cached = cached;
    }

    public String getCacheAge() {
        return cacheAge;
    }

    public void setCacheAge(String cacheAge) {
        this.cacheAge = cacheAge;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSearchTime() {
        return searchTime;
    }

    public void setSearchTime(String searchTime) {
        this.searchTime = searchTime;
    }

    public String getDataFreshness() {
        return dataFreshness;
    }

    public void setDataFreshness(String dataFreshness) {
        this.dataFreshness = dataFreshness;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
