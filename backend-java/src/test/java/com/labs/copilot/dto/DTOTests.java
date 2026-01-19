package com.labs.copilot.dto;

import com.labs.copilot.model.Product;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Unit tests for PaginatedResponse DTO.
 */
class PaginatedResponseDTOTests {

    private PaginatedResponse<Product> response;
    private List<Product> testData;

    @BeforeEach
    void setup() {
        response = new PaginatedResponse<>();
        testData = new ArrayList<>();
        
        Product p1 = new Product(UUID.randomUUID(), "Product1", new BigDecimal("10.00"), "Cat1", "SKU1");
        Product p2 = new Product(UUID.randomUUID(), "Product2", new BigDecimal("20.00"), "Cat2", "SKU2");
        testData.add(p1);
        testData.add(p2);
    }

    @Test
    @DisplayName("Should create PaginatedResponse with default constructor")
    void testPaginatedResponseDefaultConstructor() {
        PaginatedResponse<Product> resp = new PaginatedResponse<>();

        assertNotNull(resp);
        assertNull(resp.getData());
        assertNull(resp.getPagination());
        assertNull(resp.get_metadata());
    }

    @Test
    @DisplayName("Should create PaginatedResponse with data and pagination")
    void testPaginatedResponseConstructor() {
        PaginatedResponse.PaginationInfo pagination = new PaginatedResponse.PaginationInfo(1, 20, 100L, 5);
        PaginatedResponse<Product> resp = new PaginatedResponse<>(testData, pagination);

        assertEquals(testData, resp.getData());
        assertEquals(pagination, resp.getPagination());
    }

    @Test
    @DisplayName("Should set and get data")
    void testSetGetData() {
        response.setData(testData);

        assertEquals(testData, response.getData());
        assertEquals(2, response.getData().size());
    }

    @Test
    @DisplayName("Should set and get pagination info")
    void testSetGetPagination() {
        PaginatedResponse.PaginationInfo pagination = new PaginatedResponse.PaginationInfo(2, 10, 50L, 5);
        response.setPagination(pagination);

        assertEquals(pagination, response.getPagination());
        assertEquals(2, response.getPagination().getPage());
        assertEquals(10, response.getPagination().getPageSize());
    }

    @Test
    @DisplayName("Should set and get metadata")
    void testSetGetMetadata() {
        ResponseMetadata metadata = new ResponseMetadata();
        metadata.setSource("mongodb");
        response.set_metadata(metadata);

        assertEquals(metadata, response.get_metadata());
        assertEquals("mongodb", response.get_metadata().getSource());
    }

    @Test
    @DisplayName("Should handle null data")
    void testNullData() {
        response.setData(null);

        assertNull(response.getData());
    }

    @Test
    @DisplayName("Should handle empty data list")
    void testEmptyData() {
        response.setData(new ArrayList<>());

        assertNotNull(response.getData());
        assertTrue(response.getData().isEmpty());
    }

    @Test
    @DisplayName("Should maintain data integrity after multiple operations")
    void testDataIntegrity() {
        response.setData(testData);
        
        PaginatedResponse.PaginationInfo pagination = new PaginatedResponse.PaginationInfo(1, 20, 100L, 5);
        response.setPagination(pagination);

        ResponseMetadata metadata = new ResponseMetadata();
        response.set_metadata(metadata);

        assertEquals(2, response.getData().size());
        assertEquals(1, response.getPagination().getPage());
        assertNotNull(response.get_metadata());
    }
}

/**
 * Unit tests for PaginationInfo nested class.
 */
@DisplayName("PaginationInfo Tests")
class PaginationInfoNestedTests {

    @Test
    @DisplayName("Should create PaginationInfo with all fields")
    void testPaginationInfoCreation() {
        PaginatedResponse.PaginationInfo pagination = new PaginatedResponse.PaginationInfo(2, 20, 100L, 5);

        assertEquals(2, pagination.getPage());
        assertEquals(20, pagination.getPageSize());
        assertEquals(100, pagination.getTotalCount());
        assertEquals(5, pagination.getTotalPages());
    }

    @Test
    @DisplayName("Should set and get page")
    void testSetGetPage() {
        PaginatedResponse.PaginationInfo pagination = new PaginatedResponse.PaginationInfo(1, 20, 100L, 5);
        pagination.setPage(3);

        assertEquals(3, pagination.getPage());
    }

    @Test
    @DisplayName("Should set and get pageSize")
    void testSetGetPageSize() {
        PaginatedResponse.PaginationInfo pagination = new PaginatedResponse.PaginationInfo(1, 20, 100L, 5);
        pagination.setPageSize(50);

        assertEquals(50, pagination.getPageSize());
    }

    @Test
    @DisplayName("Should set and get totalCount")
    void testSetGetTotalCount() {
        PaginatedResponse.PaginationInfo pagination = new PaginatedResponse.PaginationInfo(1, 20, 100L, 5);
        pagination.setTotalCount(200L);

        assertEquals(200, pagination.getTotalCount());
    }

    @Test
    @DisplayName("Should set and get totalPages")
    void testSetGetTotalPages() {
        PaginatedResponse.PaginationInfo pagination = new PaginatedResponse.PaginationInfo(1, 20, 100L, 5);
        pagination.setTotalPages(10);

        assertEquals(10, pagination.getTotalPages());
    }

    @Test
    @DisplayName("Should handle boundary values for pagination")
    void testPaginationBoundaryValues() {
        PaginatedResponse.PaginationInfo pagination = new PaginatedResponse.PaginationInfo(1, 1, 1L, 1);

        assertEquals(1, pagination.getPage());
        assertEquals(1, pagination.getPageSize());
        assertEquals(1, pagination.getTotalCount());
        assertEquals(1, pagination.getTotalPages());
    }
}

/**
 * Unit tests for ResponseMetadata DTO.
 */
@DisplayName("ResponseMetadata DTO Tests")
class ResponseMetadataDTOTests {

    @Test
    @DisplayName("Should create ResponseMetadata with default constructor")
    void testResponseMetadataCreation() {
        ResponseMetadata metadata = new ResponseMetadata();

        assertNotNull(metadata);
        assertNull(metadata.getSource());
        assertNotNull(metadata.getTimestamp());
    }

    @Test
    @DisplayName("Should set and get source")
    void testSetGetSource() {
        ResponseMetadata metadata = new ResponseMetadata();
        metadata.setSource("redis");

        assertEquals("redis", metadata.getSource());
    }

    @Test
    @DisplayName("Should set and get cached flag")
    void testSetGetCached() {
        ResponseMetadata metadata = new ResponseMetadata();
        metadata.setCached(true);

        assertTrue(metadata.getCached());
    }

    @Test
    @DisplayName("Should set and get cache age")
    void testSetGetCacheAge() {
        ResponseMetadata metadata = new ResponseMetadata();
        metadata.setCacheAge("120s");

        assertEquals("120s", metadata.getCacheAge());
    }

    @Test
    @DisplayName("Should set and get timestamp")
    void testSetGetTimestamp() {
        ResponseMetadata metadata = new ResponseMetadata();
        LocalDateTime now = LocalDateTime.now();
        metadata.setTimestamp(now);

        assertEquals(now, metadata.getTimestamp());
    }

    @Test
    @DisplayName("Should set and get search time")
    void testSetGetSearchTime() {
        ResponseMetadata metadata = new ResponseMetadata();
        metadata.setSearchTime("45ms");

        assertEquals("45ms", metadata.getSearchTime());
    }

    @Test
    @DisplayName("Should set and get data freshness")
    void testSetGetDataFreshness() {
        ResponseMetadata metadata = new ResponseMetadata();
        metadata.setDataFreshness("current");

        assertEquals("current", metadata.getDataFreshness());
    }

    @Test
    @DisplayName("Should handle all metadata fields together")
    void testMetadataAllFields() {
        ResponseMetadata metadata = new ResponseMetadata();
        LocalDateTime now = LocalDateTime.now();

        metadata.setSource("mongodb");
        metadata.setCached(true);
        metadata.setCacheAge("300s");
        metadata.setTimestamp(now);
        metadata.setSearchTime("25ms");
        metadata.setDataFreshness("current");

        assertEquals("mongodb", metadata.getSource());
        assertTrue(metadata.getCached());
        assertEquals("300s", metadata.getCacheAge());
        assertEquals(now, metadata.getTimestamp());
        assertEquals("25ms", metadata.getSearchTime());
        assertEquals("current", metadata.getDataFreshness());
    }

    @Test
    @DisplayName("Should support various cache sources")
    void testVariousCacheSources() {
        ResponseMetadata metadata;

        String[] sources = {"redis", "mongodb", "elasticsearch", "postgresql"};
        for (String source : sources) {
            metadata = new ResponseMetadata();
            metadata.setSource(source);
            assertEquals(source, metadata.getSource());
        }
    }
}

/**
 * Unit tests for ErrorResponse DTO.
 */
@DisplayName("ErrorResponse DTO Tests")
class ErrorResponseDTOTests {

    @Test
    @DisplayName("Should create ErrorResponse with all fields")
    void testErrorResponseCreation() {
        ErrorResponse error = new ErrorResponse("Not found", "NOT_FOUND", "/api/v1/products/123");

        assertEquals("Not found", error.getError());
        assertEquals("NOT_FOUND", error.getCode());
        assertEquals("/api/v1/products/123", error.getPath());
    }

    @Test
    @DisplayName("Should set and get message")
    void testSetGetMessage() {
        ErrorResponse error = new ErrorResponse();
        error.setError("Product not found");

        assertEquals("Product not found", error.getError());
    }

    @Test
    @DisplayName("Should set and get error code")
    void testSetGetErrorCode() {
        ErrorResponse error = new ErrorResponse();
        error.setCode("PRODUCT_NOT_FOUND");

        assertEquals("PRODUCT_NOT_FOUND", error.getCode());
    }

    @Test
    @DisplayName("Should set and get path")
    void testSetGetPath() {
        ErrorResponse error = new ErrorResponse();
        error.setPath("/api/v1/products/invalid-id");

        assertEquals("/api/v1/products/invalid-id", error.getPath());
    }

    @Test
    @DisplayName("Should handle common error codes")
    void testCommonErrorCodes() {
        String[] errorCodes = {
            "PRODUCT_NOT_FOUND",
            "INVALID_ID_FORMAT",
            "INVALID_PAGE_NUMBER",
            "INVALID_FILTER",
            "INTERNAL_SERVER_ERROR"
        };

        for (String code : errorCodes) {
            ErrorResponse error = new ErrorResponse("Error", code, "/api/path");
            assertEquals(code, error.getCode());
        }
    }

    @Test
    @DisplayName("Should maintain error response integrity")
    void testErrorResponseIntegrity() {
        ErrorResponse error = new ErrorResponse(
            "Validation failed",
            "INVALID_INPUT",
            "/api/v1/search"
        );

        assertEquals("Validation failed", error.getError());
        assertEquals("INVALID_INPUT", error.getCode());
        assertEquals("/api/v1/search", error.getPath());
    }

    @Test
    @DisplayName("Should handle null fields gracefully")
    void testNullFields() {
        ErrorResponse error = new ErrorResponse(null, null, null);

        assertNull(error.getError());
        assertNull(error.getCode());
        assertNull(error.getPath());
    }
}
