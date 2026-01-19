# backend-dotnet 🟣

## Overview

ASP.NET Core 8 Web API serving as the **primary backend service** for the Product Catalog Management system. This service is the **single source of truth** for all product data and handles all CRUD operations with full validation, logging, and error handling.

## Features ✨

- **Complete Product CRUD Operations**: Create, Read, Update, Patch, Delete
- **Pagination & Sorting**: Flexible data retrieval with validation
- **Product Status Management**: ACTIVE, INACTIVE, DISCONTINUED states
- **Product Images**: Support for multiple images per product
- **SKU Uniqueness**: Database-enforced unique constraint
- **CORS Enabled**: Configured for React frontend (localhost:5173)
- **Structured Logging**: Comprehensive logging with Microsoft.Extensions.Logging
- **Error Handling**: Standardized error responses with proper HTTP status codes
- **Input Validation**: DataAnnotations validation with detailed error messages
- **Swagger/OpenAPI**: Interactive API documentation at `/swagger`
- **SQLite Database**: Lightweight database with Entity Framework Core

## Prerequisites 🔧

- .NET 8 SDK installed (run `dotnet --info` to verify)
- Recommended: Visual Studio, Rider, or VS Code with C# support

## Project Structure 📁

```
backend-dotnet/
├── Program.cs              # App entry point with Minimal API endpoints
├── appsettings.json        # Configuration (database connection)
├── backend-dotnet.csproj   # Project file with dependencies
├── Data/
│   └── ProductCatalogContext.cs    # EF Core DbContext
├── Models/
│   └── Product.cs          # Product & ProductImage entities
├── DTOs/
│   ├── ProductDTOs.cs      # Request/Response DTOs
│   └── UpdateStatusRequest.cs
└── Services/
    └── ProductService.cs   # Business logic layer
```

## API Endpoints 🚀

All endpoints are prefixed with `/api/v1`:

| Method | Endpoint                | Description                               |
| ------ | ----------------------- | ----------------------------------------- |
| POST   | `/products`             | Create a new product                      |
| GET    | `/products`             | List products (with pagination & sorting) |
| GET    | `/products/{id}`        | Get product by ID                         |
| PUT    | `/products/{id}`        | Update entire product                     |
| PATCH  | `/products/{id}`        | Partially update product                  |
| DELETE | `/products/{id}`        | Delete product (hard delete)              |
| PATCH  | `/products/{id}/status` | Update product status (soft delete)       |

### Query Parameters (GET /products)

- `page` (int, default: 1) - Page number
- `pageSize` (int, default: 20) - Items per page
- `sortBy` (string) - Sort field: Name, Price, Category, StockQuantity, CreatedAt, UpdatedAt
- `sortOrder` (string) - Sort direction: asc or desc

## Build & Run ⚙️

### Development

From the `backend-dotnet/` folder:

```bash
# Restore dependencies
dotnet restore

# Build
dotnet build

# Run
dotnet run
```

The API will start at **http://localhost:5000**

### Quick Test

```bash
# Health check
curl http://localhost:5000/

# Create a product
curl -X POST http://localhost:5000/api/v1/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop",
    "price": 999.99,
    "category": "Electronics",
    "sku": "LAPTOP-001",
    "stockQuantity": 50,
    "description": "High-performance laptop"
  }'

# Get all products
curl http://localhost:5000/api/v1/products
```

### Swagger UI

Interactive API documentation is available at:

```
http://localhost:5000/swagger
```

### Production Build

```bash
dotnet publish -c Release -o out
dotnet out/backend-dotnet.dll
```

## Database 🗄️

The application uses **SQLite** for development. The database file `product_catalog.db` is automatically created on first run.

### Database Schema

**Products Table:**

- Id (GUID, Primary Key)
- Name (string, required, max 100 chars)
- Description (string, max 500 chars)
- Price (decimal, required, > 0)
- Category (string, required, max 50 chars)
- StockQuantity (int, >= 0)
- Sku (string, required, unique, max 50 chars)
- AttributesJson (JSON string)
- Status (enum: ACTIVE, INACTIVE, DISCONTINUED)
- CreatedAt, UpdatedAt (DateTime)
- CreatedBy, UpdatedBy (string)

**ProductImages Table:**

- Id (GUID, Primary Key)
- Url (string)
- Alt (string)
- Primary (boolean)
- ProductId (GUID, Foreign Key)

### Reset Database

```bash
rm product_catalog.db
dotnet run  # Database will be recreated
```

## Configuration ⚙️

### appsettings.json

```json
{
  "ConnectionStrings": {
    "ProductCatalog": "Data Source=product_catalog.db"
  },
  "Logging": {
    "LogLevel": {
      "Default": "Information",
      "Microsoft.AspNetCore": "Warning"
    }
  }
}
```

### CORS Configuration

The API is configured to accept requests from:

- `http://localhost:5173` (React frontend)

To add more origins, edit the CORS policy in `Program.cs`.

## Error Handling 🚨

All errors return a consistent format:

```json
{
  "error": "User-friendly message",
  "code": "ERROR_CODE",
  "details": {
    "fieldName": ["Validation error message"]
  },
  "timestamp": "2026-01-19T12:00:00Z",
  "path": "/api/v1/products"
}
```

### Common Error Codes

| Code              | Status | Description             |
| ----------------- | ------ | ----------------------- |
| VALIDATION_ERROR  | 400    | Input validation failed |
| PRODUCT_NOT_FOUND | 404    | Product doesn't exist   |
| DUPLICATE_SKU     | 409    | SKU already exists      |

## Architecture Alignment 🏗️

This service follows the architecture defined in `/docs/ARCHITECTURE.md`:

- **Role**: Primary source of truth for product data
- **Responsibilities**: All write operations (CREATE, UPDATE, DELETE)
- **Port**: 5000
- **Database**: SQLite (development), PostgreSQL (production)
- **CORS**: Enabled for frontend at localhost:5173
- **API Gateway**: Routes `/api/v1/products/*` to this service

## Security Features 🔒

- ✅ CORS protection
- ✅ Input validation
- ✅ SQL injection prevention (EF Core parameterized queries)
- ✅ Sort field whitelisting
- ✅ Error message sanitization (no internal details exposed)
- ⚠️ Authentication/Authorization not implemented (use X-User-ID header)

## Logging 📝

Structured logging is enabled throughout:

```
Information: Normal operations (create, update, fetch)
Warning: Not found, duplicate SKU, invalid requests
Error: Exceptions and unexpected failures
```

View logs in the console output.

## Tests 🧪

No test projects are included. To add tests:

```bash
# Create test project
dotnet new xunit -n backend-dotnet.Tests

# Add reference
cd backend-dotnet.Tests
dotnet add reference ../backend-dotnet.csproj

# Run tests
dotnet test
```

## Troubleshooting ⚠️

### Port Already in Use

```bash
# Check what's using port 5000
lsof -i :5000

# Use a different port
dotnet run --urls "http://localhost:5001"
```

### Database Issues

```bash
# Delete and recreate database
rm product_catalog.db
dotnet run
```

### CORS Errors from Frontend

Ensure the frontend is running on `http://localhost:5173`. If using a different port, update the CORS policy in `Program.cs`.

### Build Errors

```bash
# Clean and rebuild
dotnet clean
dotnet restore
dotnet build
```

### Check Runtime

```bash
dotnet --info
# Ensure .NET 8.0 is installed
```

## Dependencies 📦

- Microsoft.EntityFrameworkCore (8.0.0)
- Microsoft.EntityFrameworkCore.Sqlite (8.0.0)
- Microsoft.EntityFrameworkCore.Design (8.0.0)
- Microsoft.AspNetCore.OpenApi (8.0.0)
- Swashbuckle.AspNetCore (6.5.0)

## Future Enhancements 🚀

- [ ] Add unit and integration tests
- [ ] Implement JWT authentication
- [ ] Add Redis caching layer
- [ ] PostgreSQL support for production
- [ ] Rate limiting middleware
- [ ] Health check endpoints
- [ ] Docker containerization
- [ ] CI/CD pipeline

---

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch
3. Add tests for new features
4. Submit a pull request

---

## License

MIT

---

**Need Help?** Check the architecture documentation at `/docs/ARCHITECTURE.md`
