package com.labs.copilot.service;

import com.labs.copilot.model.Product;
import com.labs.copilot.model.ProductStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Product service for searching and retrieving products.
 * This is a read-only service that queries the MongoDB read-replica.
 * In production, this would connect to a database or Elasticsearch.
 */
@Service
public class ProductService {

    // Mock product database (in production, this would be MongoDB/Elasticsearch)
    private List<Product> mockProducts;

    public ProductService() {
        initializeMockProducts();
    }

    /**
     * Initialize mock product data for demonstration.
     */
    private void initializeMockProducts() {
        mockProducts = new ArrayList<>();
        
        // Sample products
        Product p1 = new Product(UUID.randomUUID(), "Laptop", new BigDecimal("999.99"), "Electronics", "LAPTOP-001");
        p1.setDescription("High-performance laptop for developers");
        p1.setStockQuantity(50);
        p1.setStatus(ProductStatus.ACTIVE);
        p1.setCreatedAt(LocalDateTime.now().minusDays(30));
        p1.setUpdatedAt(LocalDateTime.now().minusDays(5));
        mockProducts.add(p1);

        Product p2 = new Product(UUID.randomUUID(), "Wireless Mouse", new BigDecimal("29.99"), "Electronics", "MOUSE-001");
        p2.setDescription("Ergonomic wireless mouse with extended battery");
        p2.setStockQuantity(200);
        p2.setStatus(ProductStatus.ACTIVE);
        p2.setCreatedAt(LocalDateTime.now().minusDays(60));
        p2.setUpdatedAt(LocalDateTime.now().minusDays(2));
        mockProducts.add(p2);

        Product p3 = new Product(UUID.randomUUID(), "USB-C Hub", new BigDecimal("49.99"), "Electronics", "HUB-001");
        p3.setDescription("Multi-port USB-C hub with HDMI and SD card reader");
        p3.setStockQuantity(120);
        p3.setStatus(ProductStatus.ACTIVE);
        p3.setCreatedAt(LocalDateTime.now().minusDays(45));
        p3.setUpdatedAt(LocalDateTime.now().minusDays(1));
        mockProducts.add(p3);

        Product p4 = new Product(UUID.randomUUID(), "Mechanical Keyboard", new BigDecimal("149.99"), "Electronics", "KB-001");
        p4.setDescription("RGB mechanical keyboard with hot-swappable switches");
        p4.setStockQuantity(75);
        p4.setStatus(ProductStatus.ACTIVE);
        p4.setCreatedAt(LocalDateTime.now().minusDays(20));
        p4.setUpdatedAt(LocalDateTime.now());
        mockProducts.add(p4);

        Product p5 = new Product(UUID.randomUUID(), "Monitor Stand", new BigDecimal("39.99"), "Office", "STAND-001");
        p5.setDescription("Adjustable monitor stand with storage drawer");
        p5.setStockQuantity(0);
        p5.setStatus(ProductStatus.INACTIVE);
        p5.setCreatedAt(LocalDateTime.now().minusDays(15));
        p5.setUpdatedAt(LocalDateTime.now().minusDays(7));
        mockProducts.add(p5);
    }

    /**
     * Get all products with pagination.
     *
     * @param page     page number (1-based)
     * @param pageSize number of items per page
     * @param sortBy   field to sort by (default: name)
     * @param sortOrder sort order (asc or desc)
     * @return paginated list of products
     */
    public SearchResult searchProducts(Integer page, Integer pageSize, String sortBy, String sortOrder) {
        page = page != null && page > 0 ? page : 1;
        pageSize = pageSize != null && pageSize > 0 ? pageSize : 20;
        pageSize = Math.min(pageSize, 100); // Max 100 items per page
        sortBy = sortBy != null ? sortBy : "name";
        sortOrder = sortOrder != null && sortOrder.equalsIgnoreCase("desc") ? "desc" : "asc";

        // Filter only active products
        List<Product> activeProducts = mockProducts.stream()
                .filter(p -> p.getStatus() == ProductStatus.ACTIVE)
                .collect(Collectors.toList());

        // Sort
        final String finalSortBy = sortBy;
        final String finalSortOrder = sortOrder;
        activeProducts.sort((p1, p2) -> {
            int comparison = switch (finalSortBy.toLowerCase()) {
                case "price" -> p1.getPrice().compareTo(p2.getPrice());
                case "created" -> p1.getCreatedAt().compareTo(p2.getCreatedAt());
                default -> p1.getName().compareTo(p2.getName());
            };
            return finalSortOrder.equalsIgnoreCase("desc") ? -comparison : comparison;
        });

        // Paginate
        int totalCount = activeProducts.size();
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalCount);

        List<Product> paginatedProducts = startIndex < totalCount
                ? activeProducts.subList(startIndex, endIndex)
                : new ArrayList<>();

        return new SearchResult(paginatedProducts, page, pageSize, (long) totalCount, totalPages, true, "mongodb");
    }

    /**
     * Search products by query and filters.
     *
     * @param query      search query
     * @param category   filter by category
     * @param minPrice   minimum price
     * @param maxPrice   maximum price
     * @param inStock    filter by stock availability
     * @param page       page number
     * @param pageSize   items per page
     * @return search results
     */
    public SearchResult searchByFilters(String query, String category, BigDecimal minPrice, BigDecimal maxPrice,
                                       Boolean inStock, Integer page, Integer pageSize) {
        page = page != null && page > 0 ? page : 1;
        pageSize = pageSize != null && pageSize > 0 ? pageSize : 20;
        pageSize = Math.min(pageSize, 100);

        List<Product> results = mockProducts.stream()
                .filter(p -> p.getStatus() == ProductStatus.ACTIVE || (inStock != null && !inStock))
                .filter(p -> query == null || p.getName().toLowerCase().contains(query.toLowerCase()) ||
                        (p.getDescription() != null && p.getDescription().toLowerCase().contains(query.toLowerCase())))
                .filter(p -> category == null || p.getCategory().equalsIgnoreCase(category))
                .filter(p -> minPrice == null || p.getPrice().compareTo(minPrice) >= 0)
                .filter(p -> maxPrice == null || p.getPrice().compareTo(maxPrice) <= 0)
                .filter(p -> inStock == null || !inStock || p.getStockQuantity() > 0)
                .collect(Collectors.toList());

        int totalCount = results.size();
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalCount);

        List<Product> paginatedResults = startIndex < totalCount
                ? results.subList(startIndex, endIndex)
                : new ArrayList<>();

        return new SearchResult(paginatedResults, page, pageSize, (long) totalCount, totalPages, false, "elasticsearch");
    }

    /**
     * Get a product by ID.
     *
     * @param id product UUID
     * @return product if found
     */
    public Optional<Product> getProductById(UUID id) {
        return mockProducts.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    /**
     * Get products by category.
     *
     * @param category product category
     * @param page     page number
     * @param pageSize items per page
     * @return paginated results
     */
    public SearchResult getByCategory(String category, Integer page, Integer pageSize) {
        return searchByFilters(null, category, null, null, true, page, pageSize);
    }

    /**
     * Search result wrapper.
     */
    public static class SearchResult {
        public List<Product> products;
        public int page;
        public int pageSize;
        public long totalCount;
        public int totalPages;
        public boolean cached;
        public String source;

        public SearchResult(List<Product> products, int page, int pageSize, long totalCount, int totalPages,
                          boolean cached, String source) {
            this.products = products;
            this.page = page;
            this.pageSize = pageSize;
            this.totalCount = totalCount;
            this.totalPages = totalPages;
            this.cached = cached;
            this.source = source;
        }
    }
}
