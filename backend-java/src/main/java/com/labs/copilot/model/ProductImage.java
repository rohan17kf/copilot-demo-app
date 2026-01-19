package com.labs.copilot.model;

/**
 * Product image representation.
 */
public class ProductImage {
    private String url;
    private String alt;
    private Boolean primary;

    public ProductImage() {
    }

    public ProductImage(String url, String alt, Boolean primary) {
        this.url = url;
        this.alt = alt;
        this.primary = primary;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public Boolean getPrimary() {
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }
}
