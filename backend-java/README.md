# backend-java ☕

## Overview

Spring Boot REST service for product search and analytics (CESAI-10 feature).

**Service Type**: Read-only product catalog API (Java service in multi-service architecture)  
**Port**: `8080` (proxied via API Gateway at `localhost:3000/api/v1/`)  
**Technology**: Spring Boot 3.x, Java 17, RESTful API

This service is responsible for:
- Product search with filtering and full-text capabilities
- Product catalog queries (read-only)
- Category-based navigation
- Analytics and reporting
- Eventual consistency with .NET service via MongoDB read-replica

**Note**: All write operations (CREATE, UPDATE, DELETE) are handled by the .NET service. This service implements search and read operations only.

## API Endpoints

| Method | Endpoint | Purpose |
|--------|----------|---------|
| `GET` | `/api/v1/products` | Get all products with pagination |
| `GET` | `/api/v1/products/{productId}` | Get single product by ID |
| `GET` | `/api/v1/search` | Search products with filters |
| `GET` | `/api/v1/products/category/{category}` | Get products by category |
| `GET` | `/api/v1/health` | Health check |

### Example Requests

**Get Products with Pagination**
```bash
curl "http://localhost:8080/api/v1/products?page=1&pageSize=20&sortBy=name&sortOrder=asc"
```

**Search Products**
```bash
curl "http://localhost:8080/api/v1/search?query=laptop&category=Electronics&minPrice=500&maxPrice=1500&inStock=true"
```

**Get Single Product**
```bash
curl "http://localhost:8080/api/v1/products/{productId}"
```

## Response Format

All endpoints return paginated responses with metadata:

```json
{
  "data": [
    {
      "id": "uuid",
      "name": "Product Name",
      "price": 99.99,
      "category": "Electronics",
      "status": "ACTIVE",
      "stockQuantity": 50
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
    "dataFreshness": "current"
  }
}
```

## Prerequisites 🔧

- Java JDK 17+ (verify with `java -version`)
- Maven 3.6+ (verify with `mvn -v`)
- Optional: an IDE (IntelliJ IDEA, Eclipse, VS Code + Java extension)

## Project layout 📁

- `pom.xml` — Maven project file (Java 17 configured)
- `src/main/java/com/labs/copilot/`
  - `BackendJavaApplication.java` — Main application entry point
  - `model/` — Product entity and enums
  - `service/` — ProductService with search logic
  - `controller/` — ProductController with REST endpoints
  - `dto/` — Response DTOs (PaginatedResponse, ErrorResponse, etc.)
- `src/main/resources/application.properties` — Configuration
- `target/` — Build output

## Build & Run ⚙️

From the `backend-java/` folder:

- Build and package:

```bash
# macOS / Linux (Maven wrapper)
./mvnw clean package
# Windows (cmd)
mvnw.cmd clean package
# or if you have Maven installed
mvn clean package
```

- Run with Maven:

```bash
# macOS / Linux (Maven wrapper)
./mvnw spring-boot:run
# Windows (cmd)
mvnw.cmd spring-boot:run
# or with installed Maven
mvn spring-boot:run
```

- Run the packaged JAR:

```bash
java -jar target/*.jar
```

- Override the server port at runtime:

```bash
# with Spring Boot CLI args
java -jar target/*.jar --server.port=9090
# or with spring-boot:run and passing args
# macOS / Linux (wrapper)
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=9090
# Windows (cmd)
mvnw.cmd spring-boot:run -Dspring-boot.run.arguments=--server.port=9090
# or with installed Maven
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=9090
```

Default URL: `http://localhost:8080`

Quick check:

```bash
curl http://localhost:8080/
```

## Tests 🧪

If tests are added, run them with:

```bash
# macOS / Linux (wrapper)
./mvnw test
# Windows (cmd)
mvnw.cmd test
# or with installed Maven
mvn test
```

## Docker (optional)

Example Dockerfile you can add for containerized runs:

```dockerfile
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
```

Build and run:

```bash
docker build -t backend-java .
docker run -p 8080:8080 backend-java:latest
```

## Troubleshooting & tips ⚠️

- Check Java & Maven versions (`java -version`, `mvn -v`).
- If the port is in use, override with `--server.port` as shown above.
- When changing configuration, restart the app to pick up changes.

---

## Contributing

Add tests, update the README, and consider adding CI (GitHub Actions) or a `docker-compose.yml` if you want to run multiple services together.

---

License: MIT
