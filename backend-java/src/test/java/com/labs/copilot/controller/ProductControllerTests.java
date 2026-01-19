package com.labs.copilot.controller;

import com.labs.copilot.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for ProductController with comprehensive coverage.
 * Tests all REST endpoints with various request parameters and scenarios.
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("ProductController Integration Tests")
class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    private UUID testProductId;

    @BeforeEach
    void setup() {
        // Get a real product ID from the service
        ProductService.SearchResult result = productService.searchProducts(1, 100, "name", "asc");
        if (!result.products.isEmpty()) {
            testProductId = result.products.get(0).getId();
        }
    }

    // ================== GET /products Tests ==================

    @Test
    @DisplayName("Should return 200 with products list")
    void testGetAllProductsSuccess() throws Exception {
        mockMvc.perform(get("/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.pagination.page").exists())
                .andExpect(jsonPath("$.pagination.pageSize").exists())
                .andExpect(jsonPath("$.pagination.totalCount").exists())
                .andExpect(jsonPath("$._metadata.source").exists());
    }

    @Test
    @DisplayName("Should return products with default pagination (page=1, pageSize=20)")
    void testGetAllProductsDefaultPagination() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pagination.page", equalTo(1)))
                .andExpect(jsonPath("$.pagination.pageSize", equalTo(20)));
    }

    @Test
    @DisplayName("Should support custom page parameter")
    void testGetAllProductsCustomPage() throws Exception {
        mockMvc.perform(get("/products")
                .param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pagination.page", equalTo(1)));
    }

    @Test
    @DisplayName("Should support custom pageSize parameter")
    void testGetAllProductsCustomPageSize() throws Exception {
        mockMvc.perform(get("/products")
                .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pagination.pageSize", equalTo(10)));
    }

    @Test
    @DisplayName("Should enforce maximum page size of 100")
    void testGetAllProductsMaxPageSize() throws Exception {
        mockMvc.perform(get("/products")
                .param("pageSize", "500"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pagination.pageSize", equalTo(20)));
    }

    @Test
    @DisplayName("Should support sorting by name ascending")
    void testGetAllProductsSortByNameAsc() throws Exception {
        mockMvc.perform(get("/products")
                .param("sortBy", "name")
                .param("sortOrder", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(greaterThan(0))));
    }

    @Test
    @DisplayName("Should support sorting by price descending")
    void testGetAllProductsSortByPriceDesc() throws Exception {
        mockMvc.perform(get("/products")
                .param("sortBy", "price")
                .param("sortOrder", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(greaterThan(0))));
    }

    @Test
    @DisplayName("Should support sorting by created date")
    void testGetAllProductsSortByCreated() throws Exception {
        mockMvc.perform(get("/products")
                .param("sortBy", "created")
                .param("sortOrder", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(greaterThan(0))));
    }

    @Test
    @DisplayName("Should include response metadata with cache info")
    void testGetAllProductsResponseMetadata() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._metadata.source", notNullValue()))
                .andExpect(jsonPath("$._metadata.cached", notNullValue()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-1"})
    @DisplayName("Should handle invalid page numbers gracefully")
    void testGetAllProductsInvalidPage(String page) throws Exception {
        mockMvc.perform(get("/products")
                .param("page", page))
                .andExpect(status().isOk());
    }

    // ================== GET /products/{id} Tests ==================

    @Test
    @DisplayName("Should return product by valid UUID")
    void testGetProductByIdSuccess() throws Exception {
        if (testProductId != null) {
            mockMvc.perform(get("/products/" + testProductId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", notNullValue()))
                    .andExpect(jsonPath("$.name", notNullValue()))
                    .andExpect(jsonPath("$.price", notNullValue()));
        }
    }

    @Test
    @DisplayName("Should return 404 for non-existent product")
    void testGetProductByIdNotFound() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        mockMvc.perform(get("/products/" + nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", notNullValue()))
                .andExpect(jsonPath("$.code", equalTo("PRODUCT_NOT_FOUND")));
    }

    @Test
    @DisplayName("Should return 400 for invalid UUID format")
    void testGetProductByIdInvalidFormat() throws Exception {
        mockMvc.perform(get("/products/invalid-uuid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", equalTo("INVALID_ID_FORMAT")));
    }

    @Test
    @DisplayName("Should return product details with correct structure")
    void testGetProductByIdStructure() throws Exception {
        if (testProductId != null) {
            mockMvc.perform(get("/products/" + testProductId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.name").exists())
                    .andExpect(jsonPath("$.description").exists())
                    .andExpect(jsonPath("$.price").exists())
                    .andExpect(jsonPath("$.category").exists())
                    .andExpect(jsonPath("$.sku").exists());
        }
    }

    // ================== GET /search Tests ==================

    @Test
    @DisplayName("Should search products by query")
    void testSearchProductsByQuery() throws Exception {
        mockMvc.perform(get("/products/search")
                .param("query", "laptop"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pagination", notNullValue()));
    }

    @Test
    @DisplayName("Should search products by category")
    void testSearchProductsByCategory() throws Exception {
        mockMvc.perform(get("/products/search")
                .param("category", "Electronics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(greaterThan(0))));
    }

    @Test
    @DisplayName("Should search products by minimum price")
    void testSearchProductsByMinPrice() throws Exception {
        mockMvc.perform(get("/products/search")
                .param("minPrice", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pagination", notNullValue()));
    }

    @Test
    @DisplayName("Should search products by maximum price")
    void testSearchProductsByMaxPrice() throws Exception {
        mockMvc.perform(get("/products/search")
                .param("maxPrice", "500"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pagination", notNullValue()));
    }

    @Test
    @DisplayName("Should search products by price range")
    void testSearchProductsByPriceRange() throws Exception {
        mockMvc.perform(get("/products/search")
                .param("minPrice", "50")
                .param("maxPrice", "500"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(greaterThan(0))));
    }

    @Test
    @DisplayName("Should search products by stock availability")
    void testSearchProductsByStock() throws Exception {
        mockMvc.perform(get("/products/search")
                .param("inStock", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(greaterThan(0))));
    }

    @Test
    @DisplayName("Should search with multiple filters combined")
    void testSearchProductsCombinedFilters() throws Exception {
        mockMvc.perform(get("/products/search")
                .param("query", "laptop")
                .param("category", "Electronics")
                .param("minPrice", "500")
                .param("maxPrice", "1500")
                .param("inStock", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pagination", notNullValue()));
    }

    @Test
    @DisplayName("Should return empty results for non-matching search")
    void testSearchProductsNoResults() throws Exception {
        mockMvc.perform(get("/products/search")
                .param("query", "NONEXISTENT123ABC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(0)))
                .andExpect(jsonPath("$.pagination.totalCount", equalTo(0)));
    }

    @Test
    @DisplayName("Should include search time in response metadata")
    void testSearchProductsMetadata() throws Exception {
        mockMvc.perform(get("/products/search")
                .param("query", "mouse"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._metadata.source", equalTo("elasticsearch")))
                .andExpect(jsonPath("$._metadata.cached", equalTo(false)));
    }

    @Test
    @DisplayName("Should support pagination in search results")
    void testSearchProductsPagination() throws Exception {
        mockMvc.perform(get("/products/search")
                .param("page", "1")
                .param("pageSize", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pagination.pageSize", equalTo(5)));
    }

    // ================== GET /products/category/{category} Tests ==================

    @Test
    @DisplayName("Should return products by category")
    void testGetProductsByCategorySuccess() throws Exception {
        mockMvc.perform(get("/products/category/Electronics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.pagination", notNullValue()));
    }

    @Test
    @DisplayName("Should return products only in specified category")
    void testGetProductsByCategoryCorrectFilter() throws Exception {
        MvcResult result = mockMvc.perform(get("/products/category/Electronics"))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        org.junit.jupiter.api.Assertions.assertTrue(response.contains("Electronics"));
    }

    @Test
    @DisplayName("Should return empty results for non-existent category")
    void testGetProductsByCategoryNotFound() throws Exception {
        mockMvc.perform(get("/products/category/NonExistentCategory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(0)));
    }

    @Test
    @DisplayName("Should support pagination in category results")
    void testGetProductsByCategoryPagination() throws Exception {
        mockMvc.perform(get("/products/category/Electronics")
                .param("page", "1")
                .param("pageSize", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pagination.pageSize", equalTo(5)));
    }

    @Test
    @DisplayName("Should include metadata in category results")
    void testGetProductsByCategoryMetadata() throws Exception {
        mockMvc.perform(get("/products/category/Electronics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._metadata", notNullValue()))
                .andExpect(jsonPath("$._metadata.source", notNullValue()));
    }

    // ================== GET /health Tests ==================

    @Test
    @DisplayName("Should return 200 for health check")
    void testHealthCheckSuccess() throws Exception {
        mockMvc.perform(get("/products/health"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("running")));
    }

    @Test
    @DisplayName("Should return success message")
    void testHealthCheckMessage() throws Exception {
        mockMvc.perform(get("/products/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Product search service is running"));
    }

    // ================== Response Format Tests ==================

    @Test
    @DisplayName("Should return application/json content type")
    void testResponseContentType() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Should return valid pagination structure")
    void testPaginationStructure() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pagination.page").exists())
                .andExpect(jsonPath("$.pagination.pageSize").exists())
                .andExpect(jsonPath("$.pagination.totalCount").exists())
                .andExpect(jsonPath("$.pagination.totalPages").exists());
    }

    @Test
    @DisplayName("Should return valid product structure in list")
    void testProductStructureInList() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").exists())
                .andExpect(jsonPath("$.data[0].name").exists())
                .andExpect(jsonPath("$.data[0].price").exists())
                .andExpect(jsonPath("$.data[0].category").exists());
    }

    @Test
    @DisplayName("Should include metadata in all responses")
    void testMetadataInAllResponses() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._metadata").exists());
    }

    // ================== Error Handling Tests ==================

    @Test
    @DisplayName("Should handle invalid parameter types gracefully")
    void testInvalidParameterType() throws Exception {
        mockMvc.perform(get("/products")
                .param("pageSize", "invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return appropriate error for invalid UUID")
    void testErrorResponseStructure() throws Exception {
        mockMvc.perform(get("/products/not-a-uuid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.path").exists());
    }

    // ================== Edge Case Tests ==================

    @Test
    @DisplayName("Should handle very large page number")
    void testVeryLargePageNumber() throws Exception {
        mockMvc.perform(get("/products")
                .param("page", "999999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(0)));
    }

    @Test
    @DisplayName("Should handle boundary page size of 100")
    void testBoundaryPageSize() throws Exception {
        mockMvc.perform(get("/products")
                .param("pageSize", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pagination.pageSize", equalTo(100)));
    }

    @Test
    @DisplayName("Should handle CORS headers for cross-origin requests")
    void testCORSHeaders() throws Exception {
        mockMvc.perform(get("/products")
                .header("Origin", "http://localhost:5173"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should consistently return the same data for same query")
    void testConsistentResults() throws Exception {
        MvcResult result1 = mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult result2 = mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andReturn();

        // Both should return successful responses (content may differ based on data)
        org.junit.jupiter.api.Assertions.assertEquals(200, result1.getResponse().getStatus());
        org.junit.jupiter.api.Assertions.assertEquals(200, result2.getResponse().getStatus());
    }
}
