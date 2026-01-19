using backend_dotnet.Data;
using backend_dotnet.DTOs;
using backend_dotnet.Models;
using backend_dotnet.Services;
using Microsoft.EntityFrameworkCore;
using System.Text.Json.Serialization;
using System.ComponentModel.DataAnnotations;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddDbContext<ProductCatalogContext>(options =>
    options.UseSqlite(builder.Configuration.GetConnectionString("ProductCatalog")));

builder.Services.AddScoped<ProductService>();

// Add CORS policy for React frontend
builder.Services.AddCors(options =>
{
    options.AddPolicy("AllowReactApp", policy =>
    {
        policy.WithOrigins("http://localhost:5173")
              .AllowAnyHeader()
              .AllowAnyMethod()
              .AllowCredentials();
    });
});

builder.Services.AddControllers().AddJsonOptions(options =>
{
    options.JsonSerializerOptions.Converters.Add(new JsonStringEnumConverter());
});

builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen(options =>
{
    options.SwaggerDoc("v1", new Microsoft.OpenApi.Models.OpenApiInfo
    {
        Title = "Product Catalog API",
        Version = "v1",
        Description = "ASP.NET Core API for managing product catalog with full CRUD operations, pagination, and filtering. This is the primary backend service providing authoritative product data.",
        Contact = new Microsoft.OpenApi.Models.OpenApiContact
        {
            Name = "Product Catalog Team"
        }
    });
});

var app = builder.Build();

// Configure the HTTP request pipeline.
app.UseSwagger();
app.UseSwaggerUI(options =>
{
    options.SwaggerEndpoint("/swagger/v1/swagger.json", "Product Catalog API v1");
    options.RoutePrefix = "swagger";
    options.DocumentTitle = "Product Catalog API Documentation";
    options.DefaultModelsExpandDepth(2);
    options.DefaultModelRendering(Swashbuckle.AspNetCore.SwaggerUI.ModelRendering.Model);
    options.DisplayRequestDuration();
    options.EnableTryItOutByDefault();
});

app.UseHttpsRedirection();
app.UseCors("AllowReactApp");

// Ensure database is created
using (var scope = app.Services.CreateScope())
{
    var db = scope.ServiceProvider.GetRequiredService<ProductCatalogContext>();
    db.Database.EnsureCreated();
}

// API Routes
var api = app.MapGroup("/api/v1");

// Create Product
api.MapPost("/products", async (CreateProductRequest request, ProductService service, HttpContext context, ILogger<Program> logger) =>
{
    // Validate model
    var validationResults = new List<ValidationResult>();
    if (!Validator.TryValidateObject(request, new ValidationContext(request), validationResults, true))
    {
        var errors = validationResults.GroupBy(v => v.MemberNames.FirstOrDefault() ?? "general")
            .ToDictionary(g => g.Key, g => g.Select(v => v.ErrorMessage ?? "Validation error").ToList());
        return Results.BadRequest(new ErrorResponse
        {
            Error = "Validation failed",
            Code = "VALIDATION_ERROR",
            Details = errors,
            Timestamp = DateTime.UtcNow,
            Path = context.Request.Path
        });
    }

    try
    {
        var userId = context.Request.Headers["X-User-ID"].FirstOrDefault() ?? "system";
        logger.LogInformation("Creating product with SKU {Sku} by user {UserId}", request.Sku, userId);
        var result = await service.CreateProductAsync(request, userId);
        logger.LogInformation("Product created successfully with ID {ProductId}", result.Id);
        return Results.Created($"/api/v1/products/{result.Id}", result);
    }
    catch (InvalidOperationException ex) when (ex.Message == "DUPLICATE_SKU")
    {
        logger.LogWarning("Duplicate SKU attempted: {Sku}", request.Sku);
        return Results.Conflict(new ErrorResponse
        {
            Error = "SKU already exists",
            Code = "DUPLICATE_SKU",
            Timestamp = DateTime.UtcNow,
            Path = context.Request.Path
        });
    }
    catch (Exception ex)
    {
        logger.LogError(ex, "Error creating product with SKU {Sku}", request.Sku);
        return Results.BadRequest(new ErrorResponse
        {
            Error = "Failed to create product",
            Code = "VALIDATION_ERROR",
            Details = new Dictionary<string, List<string>> { { "general", new List<string> { "An error occurred while creating the product" } } },
            Timestamp = DateTime.UtcNow,
            Path = context.Request.Path
        });
    }
})
.WithName("CreateProduct")
.WithTags("Products")
.WithDescription("Creates a new product in the catalog")
.WithSummary("Create a new product")
.Produces<ProductResponse>(201)
.Produces<ErrorResponse>(400)
.Produces<ErrorResponse>(409);

// Get All Products
api.MapGet("/products", async (int? page, int? pageSize, string? sortBy, string? sortOrder, ProductService service, ILogger<Program> logger) =>
{
    // Validate sort field
    var allowedSortFields = new[] { "Name", "Price", "Category", "StockQuantity", "CreatedAt", "UpdatedAt" };
    if (!string.IsNullOrEmpty(sortBy) && !allowedSortFields.Contains(sortBy, StringComparer.OrdinalIgnoreCase))
    {
        logger.LogWarning("Invalid sort field requested: {SortBy}", sortBy);
        return Results.BadRequest(new ErrorResponse
        {
            Error = "Invalid sort field",
            Code = "VALIDATION_ERROR",
            Details = new Dictionary<string, List<string>> 
            { 
                { "sortBy", new List<string> { $"Sort field must be one of: {string.Join(", ", allowedSortFields)}" } } 
            },
            Timestamp = DateTime.UtcNow,
            Path = "/api/v1/products"
        });
    }

    logger.LogInformation("Fetching products - Page: {Page}, PageSize: {PageSize}, SortBy: {SortBy}", page ?? 1, pageSize ?? 20, sortBy ?? "Name");
    var result = await service.GetProductsAsync(page ?? 1, pageSize ?? 20, sortBy, sortOrder);
    return Results.Ok(result);
})
.WithName("GetProducts")
.WithTags("Products")
.WithDescription("Retrieves a paginated list of products with optional sorting. Allowed sort fields: Name, Price, Category, StockQuantity, CreatedAt, UpdatedAt.")
.WithSummary("Get all products with pagination")
.Produces<PaginatedResponse<ProductResponse>>(200)
.Produces<ErrorResponse>(400);

// Get Product by ID
api.MapGet("/products/{id:guid}", async (Guid id, ProductService service, HttpContext context, ILogger<Program> logger) =>
{
    logger.LogInformation("Fetching product {ProductId}", id);
    var result = await service.GetProductByIdAsync(id);
    if (result == null)
    {
        logger.LogWarning("Product {ProductId} not found", id);
        return Results.NotFound(new ErrorResponse
        {
            Error = "Product not found",
            Code = "PRODUCT_NOT_FOUND",
            Timestamp = DateTime.UtcNow,
            Path = context.Request.Path
        });
    }
    return Results.Ok(result);
})
.WithName("GetProduct")
.WithTags("Products")
.WithDescription("Retrieves a single product by its unique identifier")
.WithSummary("Get product by ID")
.Produces<ProductResponse>(200)
.Produces<ErrorResponse>(404);

// Update Product
api.MapPut("/products/{id:guid}", async (Guid id, UpdateProductRequest request, ProductService service, HttpContext context, ILogger<Program> logger) =>
{
    try
    {
        var userId = context.Request.Headers["X-User-ID"].FirstOrDefault() ?? "system";
        logger.LogInformation("Updating product {ProductId} by user {UserId}", id, userId);
        var result = await service.UpdateProductAsync(id, request, userId);
        logger.LogInformation("Product {ProductId} updated successfully", id);
        return Results.Ok(result);
    }
    catch (KeyNotFoundException ex)
    {
        logger.LogWarning("Product {ProductId} not found for update: {Message}", id, ex.Message);
        return Results.NotFound(new ErrorResponse
        {
            Error = "Product not found",
            Code = "PRODUCT_NOT_FOUND",
            Timestamp = DateTime.UtcNow,
            Path = context.Request.Path
        });
    }
    catch (InvalidOperationException ex) when (ex.Message == "DUPLICATE_SKU")
    {
        logger.LogWarning("Duplicate SKU attempted during update of product {ProductId}", id);
        return Results.Conflict(new ErrorResponse
        {
            Error = "SKU already exists",
            Code = "DUPLICATE_SKU",
            Timestamp = DateTime.UtcNow,
            Path = context.Request.Path
        });
    }
    catch (Exception ex)
    {
        logger.LogError(ex, "Error updating product {ProductId}", id);
        return Results.BadRequest(new ErrorResponse
        {
            Error = "Failed to update product",
            Code = "VALIDATION_ERROR",
            Details = new Dictionary<string, List<string>> { { "general", new List<string> { "An error occurred while updating the product" } } },
            Timestamp = DateTime.UtcNow,
            Path = context.Request.Path
        });
    }
})
.WithName("UpdateProduct")
.WithTags("Products")
.WithDescription("Updates all fields of an existing product. All fields in the request body will be updated.")
.WithSummary("Full update of a product")
.Produces<ProductResponse>(200)
.Produces<ErrorResponse>(400)
.Produces<ErrorResponse>(404)
.Produces<ErrorResponse>(409);

// Partial Update Product
api.MapPatch("/products/{id:guid}", async (Guid id, UpdateProductRequest request, ProductService service, HttpContext context, ILogger<Program> logger) =>
{
    try
    {
        var userId = context.Request.Headers["X-User-ID"].FirstOrDefault() ?? "system";
        logger.LogInformation("Patching product {ProductId} by user {UserId}", id, userId);
        var result = await service.UpdateProductAsync(id, request, userId);
        logger.LogInformation("Product {ProductId} patched successfully", id);
        return Results.Ok(result);
    }
    catch (KeyNotFoundException ex)
    {
        logger.LogWarning("Product {ProductId} not found for patch: {Message}", id, ex.Message);
        return Results.NotFound(new ErrorResponse
        {
            Error = "Product not found",
            Code = "PRODUCT_NOT_FOUND",
            Timestamp = DateTime.UtcNow,
            Path = context.Request.Path
        });
    }
    catch (InvalidOperationException ex) when (ex.Message == "DUPLICATE_SKU")
    {
        logger.LogWarning("Duplicate SKU attempted during patch of product {ProductId}", id);
        return Results.Conflict(new ErrorResponse
        {
            Error = "SKU already exists",
            Code = "DUPLICATE_SKU",
            Timestamp = DateTime.UtcNow,
            Path = context.Request.Path
        });
    }
    catch (Exception ex)
    {
        logger.LogError(ex, "Error patching product {ProductId}", id);
        return Results.BadRequest(new ErrorResponse
        {
            Error = "Failed to update product",
            Code = "VALIDATION_ERROR",
            Details = new Dictionary<string, List<string>> { { "general", new List<string> { "An error occurred while updating the product" } } },
            Timestamp = DateTime.UtcNow,
            Path = context.Request.Path
        });
    }
})
.WithName("PatchProduct")
.WithTags("Products")
.WithDescription("Partially updates a product. Only provided fields will be updated, others remain unchanged.")
.WithSummary("Partial update of a product")
.Produces<ProductResponse>(200)
.Produces<ErrorResponse>(400)
.Produces<ErrorResponse>(404)
.Produces<ErrorResponse>(409);

// Delete Product
api.MapDelete("/products/{id:guid}", async (Guid id, ProductService service, HttpContext context, ILogger<Program> logger) =>
{
    try
    {
        logger.LogInformation("Deleting product {ProductId}", id);
        await service.DeleteProductAsync(id);
        logger.LogInformation("Product {ProductId} deleted successfully", id);
        return Results.NoContent();
    }
    catch (KeyNotFoundException ex)
    {
        logger.LogWarning("Product {ProductId} not found for deletion: {Message}", id, ex.Message);
        return Results.NotFound(new ErrorResponse
        {
            Error = "Product not found",
            Code = "PRODUCT_NOT_FOUND",
            Timestamp = DateTime.UtcNow,
            Path = context.Request.Path
        });
    }
})
.WithName("DeleteProduct")
.WithTags("Products")
.WithDescription("Permanently deletes a product from the catalog. This operation cannot be undone.")
.WithSummary("Delete a product")
.Produces(204)
.Produces<ErrorResponse>(404);

// Update Product Status
api.MapPatch("/products/{id:guid}/status", async (Guid id, UpdateStatusRequest request, ProductService service, HttpContext context, ILogger<Program> logger) =>
{
    try
    {
        var userId = context.Request.Headers["X-User-ID"].FirstOrDefault() ?? "system";
        logger.LogInformation("Updating product {ProductId} status to {Status} by user {UserId}", id, request.Status, userId);
        var result = await service.UpdateProductStatusAsync(id, request.Status, userId);
        return Results.Ok(result);
    }
    catch (KeyNotFoundException ex)
    {
        logger.LogWarning("Product {ProductId} not found: {Message}", id, ex.Message);
        return Results.NotFound(new ErrorResponse
        {
            Error = "Product not found",
            Code = "PRODUCT_NOT_FOUND",
            Timestamp = DateTime.UtcNow,
            Path = context.Request.Path
        });
    }
})
.WithName("UpdateProductStatus")
.WithTags("Products")
.WithDescription("Updates only the status of a product. Valid statuses: ACTIVE, INACTIVE, DISCONTINUED. Use this for soft deletes by setting status to INACTIVE.")
.WithSummary("Update product status")
.Produces<ProductResponse>(200)
.Produces<ErrorResponse>(404);

app.MapGet("/", () => "Product Catalog API - .NET Backend");

app.Run("http://localhost:5000");