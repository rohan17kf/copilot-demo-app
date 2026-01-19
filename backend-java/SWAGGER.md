# Swagger/OpenAPI Documentation

This project includes comprehensive API documentation using Springdoc OpenAPI (Swagger).

## Accessing the Documentation

### 1. **Swagger UI (Interactive)**
- **URL**: http://localhost:8080/swagger-ui.html
- **Purpose**: Interactive API documentation with "Try it out" functionality
- **Features**:
  - Browse all endpoints with full descriptions
  - View request/response schemas
  - Test API calls directly from the UI
  - See example requests and responses

### 2. **OpenAPI JSON Specification**
- **URL**: http://localhost:8080/api-docs
- **Purpose**: Machine-readable API specification
- **Use Cases**:
  - Import into API clients (Postman, Insomnia)
  - Generate SDK clients
  - API validation and testing
  - Documentation generation

### 3. **OpenAPI YAML Specification**
- **URL**: http://localhost:8080/api-docs.yaml
- **Purpose**: YAML version of the specification
- **Use Cases**: Same as JSON, plus human-readable format preference

## API Documentation Structure

### **Base Information**
- **Title**: Product Catalog API - Search & Analytics
- **Version**: 1.0.0
- **Description**: Comprehensive documentation of the Java backend service
- **Contact**: Copilot Demo Team (support@copilot-demo.local)
- **License**: MIT License

### **Servers**
- **Local Development**: http://localhost:8080
  - Direct access to Java service
- **API Gateway (Production)**: http://localhost:3000
  - Through NGINX reverse proxy

## Documented Endpoints

### **1. Get All Products**
```
GET /api/v1/products
```
- **Description**: Retrieve all active products with pagination and sorting
- **Parameters**:
  - `page` (query, integer): Page number (1-based, default: 1)
  - `pageSize` (query, integer): Items per page, max 100 (default: 20)
  - `sortBy` (query, string): Sort field (name, price, created; default: name)
  - `sortOrder` (query, string): Sort order (asc, desc; default: asc)
- **Response**: PaginatedResponse with products array and metadata
- **Status Codes**: 200 OK, 400 Bad Request, 500 Internal Server Error

### **2. Get Product by ID**
```
GET /api/v1/products/{productId}
```
- **Description**: Retrieve a specific product by UUID
- **Parameters**:
  - `productId` (path, string): Product UUID
- **Response**: Single product object
- **Status Codes**: 200 OK, 400 Bad Request, 404 Not Found, 500 Internal Server Error

### **3. Search Products**
```
GET /api/v1/search
```
- **Description**: Full-text search with optional filters
- **Parameters**:
  - `query` (query, string, optional): Search term (name/description)
  - `category` (query, string, optional): Category filter
  - `minPrice` (query, decimal, optional): Minimum price
  - `maxPrice` (query, decimal, optional): Maximum price
  - `inStock` (query, boolean, optional): Filter by stock availability
  - `page` (query, integer, default: 1)
  - `pageSize` (query, integer, default: 20, max: 100)
- **Response**: PaginatedResponse with search results
- **Status Codes**: 200 OK, 400 Bad Request, 500 Internal Server Error

### **4. Get Products by Category**
```
GET /api/v1/products/category/{category}
```
- **Description**: Retrieve all products in a specific category
- **Parameters**:
  - `category` (path, string): Category name
  - `page` (query, integer, default: 1)
  - `pageSize` (query, integer, default: 20, max: 100)
- **Response**: PaginatedResponse with category products
- **Status Codes**: 200 OK, 500 Internal Server Error

### **5. Health Check**
```
GET /api/v1/health
```
- **Description**: Check if the service is running
- **Response**: Status message string
- **Status Codes**: 200 OK

## Response Schemas

### **PaginatedResponse**
```json
{
  "data": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "name": "Laptop",
      "price": 999.99,
      "category": "Electronics",
      "stockQuantity": 50,
      "sku": "LAPTOP-001",
      "status": "ACTIVE",
      "description": "High-performance laptop",
      "createdAt": "2026-01-13T10:00:00",
      "updatedAt": "2026-01-13T10:00:00"
    }
  ],
  "pagination": {
    "page": 1,
    "pageSize": 20,
    "totalCount": 150,
    "totalPages": 8
  },
  "_metadata": {
    "cached": true,
    "cacheAge": "120s",
    "source": "mongodb",
    "dataFreshness": "current",
    "timestamp": "2026-01-13T10:30:00"
  }
}
```

### **Product**
- **id** (UUID): Unique product identifier
- **name** (string, 1-100 chars): Product name
- **description** (string, 0-500 chars): Product description
- **price** (decimal): Price (> 0)
- **category** (string, 1-50 chars): Product category
- **stockQuantity** (integer, >= 0): Available stock
- **sku** (string): Unique stock keeping unit
- **images** (array): Product images with URLs
- **attributes** (object): Product attributes (color, size, etc.)
- **status** (enum): ACTIVE, INACTIVE, or DISCONTINUED
- **createdAt** (datetime): ISO8601 creation timestamp
- **updatedAt** (datetime): ISO8601 last update timestamp
- **createdBy** (string): User ID who created
- **updatedBy** (string): User ID who last updated

### **Error Response**
```json
{
  "error": "Product not found",
  "code": "PRODUCT_NOT_FOUND",
  "details": null,
  "timestamp": "2026-01-13T10:30:00",
  "path": "/api/v1/products/invalid-id"
}
```

## API Security

- **Authentication**: Bearer token (JWT) required for protected endpoints
- **Rate Limiting**: 1000 requests per minute
- **CORS**: Enabled for http://localhost:5173 (frontend)
- **Headers**:
  - `X-RateLimit-Limit`: Request quota
  - `X-RateLimit-Remaining`: Requests left
  - `X-RateLimit-Reset`: Rate limit reset timestamp

## Testing with Swagger UI

1. **Open** http://localhost:8080/swagger-ui.html
2. **Select** an endpoint to expand
3. **Click** "Try it out"
4. **Fill** in required parameters
5. **Click** "Execute"
6. **View** response with status code and headers

## Integration Examples

### **cURL**
```bash
# Get all products
curl -X GET "http://localhost:8080/api/v1/products?page=1&pageSize=20" \
  -H "Content-Type: application/json"

# Search products
curl -X GET "http://localhost:8080/api/v1/search?query=laptop&category=Electronics" \
  -H "Content-Type: application/json"
```

### **JavaScript (Fetch)**
```javascript
// Get products
fetch('http://localhost:8080/api/v1/products?page=1')
  .then(res => res.json())
  .then(data => console.log(data.data))
  .catch(err => console.error(err));

// Search products
fetch('http://localhost:8080/api/v1/search?query=laptop&category=Electronics')
  .then(res => res.json())
  .then(data => console.log(data.data))
  .catch(err => console.error(err));
```

### **Python (Requests)**
```python
import requests

# Get products
response = requests.get('http://localhost:8080/api/v1/products', 
                       params={'page': 1, 'pageSize': 20})
products = response.json()['data']

# Search products
response = requests.get('http://localhost:8080/api/v1/search',
                       params={'query': 'laptop', 'category': 'Electronics'})
results = response.json()['data']
```

## Generating API Clients

Using the OpenAPI specification, you can generate SDKs in multiple languages:

```bash
# OpenAPI Generator
openapi-generator-cli generate -i http://localhost:8080/api-docs \
  -g java -o ./java-client

# Swagger Codegen
swagger-codegen generate -i http://localhost:8080/api-docs \
  -l python -o ./python-client
```

## More Information

- **Springdoc OpenAPI**: https://springdoc.org/
- **OpenAPI Specification**: https://spec.openapis.org/
- **Swagger UI**: https://swagger.io/tools/swagger-ui/
- **Testing Swagger APIs**: https://swagger.io/resources/articles/best-practices-in-api-design/
