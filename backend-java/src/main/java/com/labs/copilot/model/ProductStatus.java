package com.labs.copilot.model;

/**
 * Product status enum.
 * ACTIVE: Product is available for purchase
 * INACTIVE: Product is temporarily unavailable
 * DISCONTINUED: Product is no longer sold
 */
public enum ProductStatus {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    DISCONTINUED("DISCONTINUED");

    private final String value;

    ProductStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ProductStatus fromValue(String value) {
        for (ProductStatus status : ProductStatus.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown product status: " + value);
    }
}
