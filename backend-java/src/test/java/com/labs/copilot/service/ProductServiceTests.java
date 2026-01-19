package com.labs.copilot.service;

import com.labs.copilot.model.Product;
import com.labs.copilot.model.ProductStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ProductService with comprehensive coverage.
 * Tests search, filter, pagination, and retrieval operations.
 */
@DisplayName("ProductService Tests")
class ProductServiceTests {

    private ProductService productService;

    @BeforeEach
    void setup() {
        productService = new ProductService();
    }

    // ================== searchProducts Tests ==================

    @Test
    @DisplayName("Should retrieve all active products with default pagination")
    void testSearchProductsDefault() {
        ProductService.SearchResult result = productService.searchProducts(1, 20, "name", "asc");

        assertNotNull(result);
        assertEquals(1, result.page);
        assertEquals(20, result.pageSize);
        assertTrue(result.totalCount > 0);
        assertFalse(result.products.isEmpty());
        assertEquals("mongodb", result.source);
        assertTrue(result.cached);
    }

    @Test
    @DisplayName("Should handle pagination - page 2")
    void testSearchProductsPagination() {
        ProductService.SearchResult result = productService.searchProducts(2, 2, "name", "asc");

        assertEquals(2, result.page);
        assertEquals(2, result.pageSize);
        assertTrue(result.totalPages >= 2);
    }

    @Test
    @DisplayName("Should sort products by name ascending")
    void testSearchProductsSortByNameAsc() {
        ProductService.SearchResult result = productService.searchProducts(1, 100, "name", "asc");

        assertTrue(result.products.size() >= 2);
        for (int i = 0; i < result.products.size() - 1; i++) {
            assertTrue(result.products.get(i).getName().compareTo(result.products.get(i + 1).getName()) <= 0);
        }
    }

    @Test
    @DisplayName("Should sort products by name descending")
    void testSearchProductsSortByNameDesc() {
        ProductService.SearchResult result = productService.searchProducts(1, 100, "name", "desc");

        assertTrue(result.products.size() >= 2);
        for (int i = 0; i < result.products.size() - 1; i++) {
            assertTrue(result.products.get(i).getName().compareTo(result.products.get(i + 1).getName()) >= 0);
        }
    }

    @Test
    @DisplayName("Should sort products by price ascending")
    void testSearchProductsSortByPriceAsc() {
        ProductService.SearchResult result = productService.searchProducts(1, 100, "price", "asc");

        assertTrue(result.products.size() >= 2);
        for (int i = 0; i < result.products.size() - 1; i++) {
            assertTrue(result.products.get(i).getPrice().compareTo(result.products.get(i + 1).getPrice()) <= 0);
        }
    }

    @Test
    @DisplayName("Should sort products by price descending")
    void testSearchProductsSortByPriceDesc() {
        ProductService.SearchResult result = productService.searchProducts(1, 100, "price", "desc");

        assertTrue(result.products.size() >= 2);
        for (int i = 0; i < result.products.size() - 1; i++) {
            assertTrue(result.products.get(i).getPrice().compareTo(result.products.get(i + 1).getPrice()) >= 0);
        }
    }

    @Test
    @DisplayName("Should sort products by created date")
    void testSearchProductsSortByCreatedDate() {
        ProductService.SearchResult result = productService.searchProducts(1, 100, "created", "asc");

        assertTrue(result.products.size() >= 1);
        assertEquals("mongodb", result.source);
    }

    @Test
    @DisplayName("Should enforce maximum page size of 100")
    void testSearchProductsMaxPageSize() {
        ProductService.SearchResult result = productService.searchProducts(1, 500, "name", "asc");

        assertEquals(100, result.pageSize);
    }

    @Test
    @DisplayName("Should handle invalid page number - default to 1")
    void testSearchProductsInvalidPageNumber() {
        ProductService.SearchResult result = productService.searchProducts(-1, 20, "name", "asc");

        assertEquals(1, result.page);
    }

    @Test
    @DisplayName("Should handle null page number - default to 1")
    void testSearchProductsNullPageNumber() {
        ProductService.SearchResult result = productService.searchProducts(null, 20, "name", "asc");

        assertEquals(1, result.page);
    }

    @Test
    @DisplayName("Should handle invalid page size - default to 20")
    void testSearchProductsInvalidPageSize() {
        ProductService.SearchResult result = productService.searchProducts(1, -1, "name", "asc");

        assertEquals(20, result.pageSize);
    }

    @Test
    @DisplayName("Should handle null page size - default to 20")
    void testSearchProductsNullPageSize() {
        ProductService.SearchResult result = productService.searchProducts(1, null, "name", "asc");

        assertEquals(20, result.pageSize);
    }

    @Test
    @DisplayName("Should handle null sortBy - default to name")
    void testSearchProductsNullSortBy() {
        ProductService.SearchResult result = productService.searchProducts(1, 20, null, "asc");

        assertNotNull(result);
        assertFalse(result.products.isEmpty());
    }

    @Test
    @DisplayName("Should handle null sortOrder - default to asc")
    void testSearchProductsNullSortOrder() {
        ProductService.SearchResult result = productService.searchProducts(1, 20, "name", null);

        assertNotNull(result);
        assertFalse(result.products.isEmpty());
    }

    @Test
    @DisplayName("Should return only active products")
    void testSearchProductsOnlyActive() {
        ProductService.SearchResult result = productService.searchProducts(1, 100, "name", "asc");

        assertTrue(result.products.stream().allMatch(p -> p.getStatus() == ProductStatus.ACTIVE));
    }

    // ================== searchByFilters Tests ==================

    @Test
    @DisplayName("Should search by text query in product name")
    void testSearchByQueryInName() {
        ProductService.SearchResult result = productService.searchByFilters("laptop", null, null, null, null, 1, 20);

        assertNotNull(result);
        assertTrue(result.products.stream().anyMatch(p -> p.getName().toLowerCase().contains("laptop")));
        assertEquals("elasticsearch", result.source);
        assertFalse(result.cached);
    }

    @Test
    @DisplayName("Should search by text query in product description")
    void testSearchByQueryInDescription() {
        ProductService.SearchResult result = productService.searchByFilters("ergonomic", null, null, null, null, 1, 20);

        assertNotNull(result);
        assertTrue(result.products.stream().anyMatch(p -> p.getDescription() != null && p.getDescription().toLowerCase().contains("ergonomic")));
    }

    @Test
    @DisplayName("Should filter products by category")
    void testSearchByCategory() {
        ProductService.SearchResult result = productService.searchByFilters(null, "Electronics", null, null, null, 1, 100);

        assertNotNull(result);
        assertTrue(result.products.stream().allMatch(p -> p.getCategory().equalsIgnoreCase("Electronics")));
    }

    @Test
    @DisplayName("Should filter products by minimum price")
    void testSearchByMinPrice() {
        BigDecimal minPrice = new BigDecimal("100");
        ProductService.SearchResult result = productService.searchByFilters(null, null, minPrice, null, null, 1, 100);

        assertNotNull(result);
        assertTrue(result.products.stream().allMatch(p -> p.getPrice().compareTo(minPrice) >= 0));
    }

    @Test
    @DisplayName("Should filter products by maximum price")
    void testSearchByMaxPrice() {
        BigDecimal maxPrice = new BigDecimal("500");
        ProductService.SearchResult result = productService.searchByFilters(null, null, null, maxPrice, null, 1, 100);

        assertNotNull(result);
        assertTrue(result.products.stream().allMatch(p -> p.getPrice().compareTo(maxPrice) <= 0));
    }

    @Test
    @DisplayName("Should filter products by price range")
    void testSearchByPriceRange() {
        BigDecimal minPrice = new BigDecimal("50");
        BigDecimal maxPrice = new BigDecimal("500");
        ProductService.SearchResult result = productService.searchByFilters(null, null, minPrice, maxPrice, null, 1, 100);

        assertNotNull(result);
        assertTrue(result.products.stream().allMatch(p -> 
            p.getPrice().compareTo(minPrice) >= 0 && p.getPrice().compareTo(maxPrice) <= 0
        ));
    }

    @Test
    @DisplayName("Should filter products by stock availability - in stock only")
    void testSearchByInStockTrue() {
        ProductService.SearchResult result = productService.searchByFilters(null, null, null, null, true, 1, 100);

        assertNotNull(result);
        assertTrue(result.products.stream().allMatch(p -> p.getStockQuantity() > 0));
    }

    @Test
    @DisplayName("Should handle combined filters - query and category")
    void testSearchByQueryAndCategory() {
        ProductService.SearchResult result = productService.searchByFilters("keyboard", "Electronics", null, null, null, 1, 100);

        assertNotNull(result);
    }

    @Test
    @DisplayName("Should handle combined filters - price range and stock")
    void testSearchByPriceRangeAndStock() {
        BigDecimal minPrice = new BigDecimal("20");
        BigDecimal maxPrice = new BigDecimal("200");
        ProductService.SearchResult result = productService.searchByFilters(null, null, minPrice, maxPrice, true, 1, 100);

        assertNotNull(result);
        assertTrue(result.products.stream().allMatch(p -> 
            p.getPrice().compareTo(minPrice) >= 0 && 
            p.getPrice().compareTo(maxPrice) <= 0 && 
            p.getStockQuantity() > 0
        ));
    }

    @Test
    @DisplayName("Should return empty results for non-matching query")
    void testSearchByNonMatchingQuery() {
        ProductService.SearchResult result = productService.searchByFilters("NONEXISTENT123", null, null, null, null, 1, 100);

        assertNotNull(result);
        assertTrue(result.products.isEmpty());
        assertEquals(0, result.totalCount);
    }

    @Test
    @DisplayName("Should return empty results for non-matching category")
    void testSearchByNonMatchingCategory() {
        ProductService.SearchResult result = productService.searchByFilters(null, "NonExistentCategory", null, null, null, 1, 100);

        assertNotNull(result);
        assertTrue(result.products.isEmpty());
    }

    @Test
    @DisplayName("Should handle pagination in filtered results")
    void testSearchByFiltersWithPagination() {
        ProductService.SearchResult result = productService.searchByFilters(null, "Electronics", null, null, null, 1, 2);

        assertEquals(1, result.page);
        assertEquals(2, result.pageSize);
    }

    // ================== getProductById Tests ==================

    @Test
    @DisplayName("Should retrieve product by existing ID")
    void testGetProductByIdExists() {
        // First, get a product ID from search results
        ProductService.SearchResult result = productService.searchProducts(1, 100, "name", "asc");
        UUID productId = result.products.get(0).getId();

        Optional<Product> product = productService.getProductById(productId);

        assertTrue(product.isPresent());
        assertEquals(productId, product.get().getId());
    }

    @Test
    @DisplayName("Should return empty optional for non-existent ID")
    void testGetProductByIdNotExists() {
        UUID nonExistentId = UUID.randomUUID();

        Optional<Product> product = productService.getProductById(nonExistentId);

        assertTrue(product.isEmpty());
    }

    @Test
    @DisplayName("Should handle null product ID gracefully")
    void testGetProductByIdNull() {
        Optional<Product> result = productService.getProductById(null);
        assertTrue(result.isEmpty());
    }

    // ================== getByCategory Tests ==================

    @Test
    @DisplayName("Should retrieve products by category")
    void testGetByCategory() {
        ProductService.SearchResult result = productService.getByCategory("Electronics", 1, 100);

        assertNotNull(result);
        assertTrue(result.products.stream().allMatch(p -> p.getCategory().equalsIgnoreCase("Electronics")));
    }

    @Test
    @DisplayName("Should return empty results for non-existent category")
    void testGetByCategoryNotExists() {
        ProductService.SearchResult result = productService.getByCategory("NonExistentCategory", 1, 100);

        assertNotNull(result);
        assertTrue(result.products.isEmpty());
    }

    @Test
    @DisplayName("Should handle pagination in category results")
    void testGetByCategoryPagination() {
        ProductService.SearchResult result = productService.getByCategory("Electronics", 1, 1);

        assertEquals(1, result.pageSize);
    }

    @Test
    @DisplayName("Should only return in-stock products from category")
    void testGetByCategoryInStockOnly() {
        ProductService.SearchResult result = productService.getByCategory("Office", 1, 100);

        assertTrue(result.products.stream().allMatch(p -> p.getStockQuantity() > 0));
    }

    // ================== Edge Cases & Boundary Tests ==================

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -100})
    @DisplayName("Should handle invalid page numbers")
    void testInvalidPageNumbers(int invalidPage) {
        ProductService.SearchResult result = productService.searchProducts(invalidPage, 20, "name", "asc");

        assertEquals(1, result.page);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -50})
    @DisplayName("Should handle invalid page sizes")
    void testInvalidPageSizes(int invalidSize) {
        ProductService.SearchResult result = productService.searchProducts(1, invalidSize, "name", "asc");

        assertEquals(20, result.pageSize);
    }

    @Test
    @DisplayName("Should have valid SearchResult structure")
    void testSearchResultStructure() {
        ProductService.SearchResult result = productService.searchProducts(1, 20, "name", "asc");

        assertNotNull(result.products);
        assertNotNull(result.source);
        assertTrue(result.totalCount >= 0);
        assertTrue(result.totalPages > 0);
    }

    @Test
    @DisplayName("Should handle multiple searches without state pollution")
    void testMultipleSearches() {
        ProductService.SearchResult result1 = productService.searchProducts(1, 10, "name", "asc");
        ProductService.SearchResult result2 = productService.searchProducts(1, 10, "price", "desc");

        assertEquals(10, result1.pageSize);
        assertEquals(10, result2.pageSize);
    }
}
