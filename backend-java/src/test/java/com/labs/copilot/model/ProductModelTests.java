package com.labs.copilot.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Product model.
 * Tests entity creation, serialization, and field validation.
 */
@DisplayName("Product Model Tests")
class ProductModelTests {

    private UUID testId;
    private LocalDateTime testDateTime;

    @BeforeEach
    void setup() {
        testId = UUID.randomUUID();
        testDateTime = LocalDateTime.now();
    }

    @Test
    @DisplayName("Should create product with required fields")
    void testProductCreationWithRequiredFields() {
        UUID id = UUID.randomUUID();
        String name = "Test Laptop";
        BigDecimal price = new BigDecimal("999.99");
        String category = "Electronics";
        String sku = "LAPTOP-001";

        Product product = new Product(id, name, price, category, sku);

        assertEquals(id, product.getId());
        assertEquals(name, product.getName());
        assertEquals(price, product.getPrice());
        assertEquals(category, product.getCategory());
        assertEquals(sku, product.getSku());
        assertEquals(ProductStatus.ACTIVE, product.getStatus());
        assertEquals(0, product.getStockQuantity());
    }

    @Test
    @DisplayName("Should create empty product with default constructor")
    void testProductDefaultConstructor() {
        Product product = new Product();

        assertNull(product.getId());
        assertNull(product.getName());
        assertNull(product.getPrice());
        assertNull(product.getCategory());
    }

    @Test
    @DisplayName("Should set and get all properties")
    void testProductGettersSetters() {
        Product product = new Product();

        product.setId(testId);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(new BigDecimal("99.99"));
        product.setCategory("Test Category");
        product.setStockQuantity(50);
        product.setSku("TEST-001");
        product.setStatus(ProductStatus.ACTIVE);
        product.setCreatedAt(testDateTime);
        product.setUpdatedAt(testDateTime);
        product.setCreatedBy("user-1");
        product.setUpdatedBy("user-2");

        assertEquals(testId, product.getId());
        assertEquals("Test Product", product.getName());
        assertEquals("Test Description", product.getDescription());
        assertEquals(new BigDecimal("99.99"), product.getPrice());
        assertEquals("Test Category", product.getCategory());
        assertEquals(50, product.getStockQuantity());
        assertEquals("TEST-001", product.getSku());
        assertEquals(ProductStatus.ACTIVE, product.getStatus());
        assertEquals(testDateTime, product.getCreatedAt());
        assertEquals(testDateTime, product.getUpdatedAt());
        assertEquals("user-1", product.getCreatedBy());
        assertEquals("user-2", product.getUpdatedBy());
    }

    @Test
    @DisplayName("Should handle product images collection")
    void testProductImages() {
        Product product = new Product();
        List<ProductImage> images = new ArrayList<>();
        ProductImage image = new ProductImage();
        image.setUrl("http://example.com/image.jpg");
        images.add(image);

        product.setImages(images);

        assertNotNull(product.getImages());
        assertEquals(1, product.getImages().size());
        assertEquals("http://example.com/image.jpg", product.getImages().get(0).getUrl());
    }

    @Test
    @DisplayName("Should handle product attributes map")
    void testProductAttributes() {
        Product product = new Product();
        Map<String, String> attributes = new HashMap<>();
        attributes.put("color", "black");
        attributes.put("size", "large");

        product.setAttributes(attributes);

        assertNotNull(product.getAttributes());
        assertEquals("black", product.getAttributes().get("color"));
        assertEquals("large", product.getAttributes().get("size"));
    }

    @Test
    @DisplayName("Should support Serializable interface")
    void testProductSerializable() {
        Product product = new Product(testId, "Test", new BigDecimal("10"), "Cat", "SKU");

        assertTrue(product instanceof java.io.Serializable);
    }

    @Test
    @DisplayName("Should support all product status values")
    void testProductStatus() {
        Product product = new Product();

        product.setStatus(ProductStatus.ACTIVE);
        assertEquals(ProductStatus.ACTIVE, product.getStatus());

        product.setStatus(ProductStatus.INACTIVE);
        assertEquals(ProductStatus.INACTIVE, product.getStatus());

        product.setStatus(ProductStatus.DISCONTINUED);
        assertEquals(ProductStatus.DISCONTINUED, product.getStatus());
    }

    @Test
    @DisplayName("Should handle decimal prices correctly")
    void testProductPriceDecimal() {
        Product product = new Product();

        BigDecimal price1 = new BigDecimal("99.99");
        product.setPrice(price1);
        assertEquals(price1, product.getPrice());

        BigDecimal price2 = new BigDecimal("1000.50");
        product.setPrice(price2);
        assertEquals(price2, product.getPrice());
    }

    @Test
    @DisplayName("Should handle null optional fields")
    void testProductOptionalFields() {
        Product product = new Product(testId, "Test", new BigDecimal("10"), "Cat", "SKU");

        product.setDescription(null);
        product.setImages(null);
        product.setAttributes(null);
        product.setCreatedBy(null);

        assertNull(product.getDescription());
        assertNull(product.getImages());
        assertNull(product.getAttributes());
        assertNull(product.getCreatedBy());
    }
}

/**
 * Unit tests for ProductStatus enum.
 */
@DisplayName("ProductStatus Enum Tests")
class ProductStatusEnumTests {

    @Test
    @DisplayName("Should have all required status values")
    void testProductStatusValues() {
        assertNotNull(ProductStatus.ACTIVE);
        assertNotNull(ProductStatus.INACTIVE);
        assertNotNull(ProductStatus.DISCONTINUED);
    }

    @Test
    @DisplayName("Should correctly identify ACTIVE status")
    void testActiveStatus() {
        assertEquals(ProductStatus.ACTIVE, ProductStatus.valueOf("ACTIVE"));
    }

    @Test
    @DisplayName("Should correctly identify INACTIVE status")
    void testInactiveStatus() {
        assertEquals(ProductStatus.INACTIVE, ProductStatus.valueOf("INACTIVE"));
    }

    @Test
    @DisplayName("Should correctly identify DISCONTINUED status")
    void testDiscontinuedStatus() {
        assertEquals(ProductStatus.DISCONTINUED, ProductStatus.valueOf("DISCONTINUED"));
    }
}

/**
 * Unit tests for ProductImage model.
 */
@DisplayName("ProductImage Model Tests")
class ProductImageModelTests {

    @Test
    @DisplayName("Should create product image with default constructor")
    void testProductImageCreation() {
        ProductImage image = new ProductImage();

        assertNotNull(image);
        assertNull(image.getUrl());
    }

    @Test
    @DisplayName("Should set and get image URL")
    void testProductImageUrl() {
        ProductImage image = new ProductImage();
        String url = "http://example.com/product.jpg";

        image.setUrl(url);

        assertEquals(url, image.getUrl());
    }

    @Test
    @DisplayName("Should handle multiple product images")
    void testMultipleProductImages() {
        ProductImage image1 = new ProductImage();
        image1.setUrl("http://example.com/image1.jpg");

        ProductImage image2 = new ProductImage();
        image2.setUrl("http://example.com/image2.jpg");

        List<ProductImage> images = Arrays.asList(image1, image2);

        assertEquals(2, images.size());
        assertEquals("http://example.com/image1.jpg", images.get(0).getUrl());
        assertEquals("http://example.com/image2.jpg", images.get(1).getUrl());
    }
}
