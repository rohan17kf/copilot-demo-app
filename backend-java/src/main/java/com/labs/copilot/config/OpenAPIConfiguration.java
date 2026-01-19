package com.labs.copilot.config;

import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI configuration for Swagger documentation.
 * OpenAPI is auto-configured by springdoc-openapi library.
 * 
 * Access documentation at: http://localhost:8080/swagger-ui.html
 * OpenAPI JSON: http://localhost:8080/api-docs
 */
@Configuration
public class OpenAPIConfiguration {
    // Springdoc-openapi automatically discovers endpoints and generates OpenAPI spec
    // No manual Bean configuration needed for basic setup
}
