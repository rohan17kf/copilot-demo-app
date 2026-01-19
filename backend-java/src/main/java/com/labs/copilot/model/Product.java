package com.labs.copilot.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Product entity representing a product in the catalog.
 * This is a read-only model synchronized from the .NET service (PostgreSQL).
 */
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    
    private String name;
    
    private String description;
    
    private BigDecimal price;
    
    private String category;
    
    private Integer stockQuantity;
    
    private String sku;
    
    private List<ProductImage> images;
    
    private Map<String, String> attributes;
    
    private ProductStatus status;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private String createdBy;
    
    private String updatedBy;

    // Constructors
    public Product() {
    }

    public Product(UUID id, String name, BigDecimal price, String category, String sku) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.sku = sku;
        this.status = ProductStatus.ACTIVE;
        this.stockQuantity = 0;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public List<ProductImage> getImages() {
        return images;
    }

    public void setImages(List<ProductImage> images) {
        this.images = images;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", sku='" + sku + '\'' +
                ", status=" + status +
                ", stockQuantity=" + stockQuantity +
                '}';
    }
}
