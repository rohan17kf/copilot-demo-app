using System.Text.Json;
using backend_dotnet.Data;
using backend_dotnet.DTOs;
using backend_dotnet.Models;
using Microsoft.EntityFrameworkCore;

namespace backend_dotnet.Services;

public class ProductService
{
    private readonly ProductCatalogContext _context;
    private readonly ILogger<ProductService> _logger;

    public ProductService(ProductCatalogContext context, ILogger<ProductService> logger)
    {
        _context = context;
        _logger = logger;
    }

    public async Task<ProductResponse> CreateProductAsync(CreateProductRequest request, string userId)
    {
        _logger.LogDebug("Checking SKU uniqueness for {Sku}", request.Sku);
        // Check if SKU already exists
        if (await _context.Products.AnyAsync(p => p.Sku == request.Sku))
        {
            _logger.LogWarning("SKU {Sku} already exists", request.Sku);
            throw new InvalidOperationException("DUPLICATE_SKU");
        }

        var product = new Product
        {
            Name = request.Name,
            Description = request.Description,
            Price = request.Price,
            Category = request.Category,
            StockQuantity = request.StockQuantity,
            Sku = request.Sku,
            AttributesJson = JsonSerializer.Serialize(request.Attributes),
            CreatedBy = userId,
            UpdatedBy = userId
        };

        foreach (var imageDto in request.Images)
        {
            product.Images.Add(new ProductImage
            {
                Url = imageDto.Url,
                Alt = imageDto.Alt,
                Primary = imageDto.Primary
            });
        }

        _context.Products.Add(product);
        await _context.SaveChangesAsync();

        return MapToResponse(product);
    }

    public async Task<PaginatedResponse<ProductResponse>> GetProductsAsync(int page = 1, int pageSize = 20, string? sortBy = null, string? sortOrder = "asc")
    {
        var query = _context.Products.AsQueryable();

        // Sorting
        if (!string.IsNullOrEmpty(sortBy))
        {
            query = sortOrder?.ToLower() == "desc"
                ? query.OrderByDescending(p => EF.Property<object>(p, sortBy))
                : query.OrderBy(p => EF.Property<object>(p, sortBy));
        }
        else
        {
            query = query.OrderBy(p => p.Name);
        }

        var totalCount = await query.CountAsync();
        var totalPages = (int)Math.Ceiling(totalCount / (double)pageSize);

        var products = await query
            .Include(p => p.Images)
            .Skip((page - 1) * pageSize)
            .Take(pageSize)
            .ToListAsync();

        var data = products.Select(MapToResponse).ToList();

        return new PaginatedResponse<ProductResponse>
        {
            Data = data,
            Pagination = new PaginationInfo
            {
                Page = page,
                PageSize = pageSize,
                TotalCount = totalCount,
                TotalPages = totalPages
            },
            Metadata = new ResponseMetadata
            {
                Cached = false,
                Source = "sqlite"
            }
        };
    }

    public async Task<ProductResponse?> GetProductByIdAsync(Guid id)
    {
        var product = await _context.Products
            .Include(p => p.Images)
            .FirstOrDefaultAsync(p => p.Id == id);

        return product == null ? null : MapToResponse(product);
    }

    public async Task<ProductResponse> UpdateProductAsync(Guid id, UpdateProductRequest request, string userId)
    {
        var product = await _context.Products
            .Include(p => p.Images)
            .FirstOrDefaultAsync(p => p.Id == id);

        if (product == null)
        {
            throw new KeyNotFoundException("PRODUCT_NOT_FOUND");
        }

        // Check SKU uniqueness if updating SKU
        if (!string.IsNullOrEmpty(request.Sku) && request.Sku != product.Sku)
        {
            if (await _context.Products.AnyAsync(p => p.Sku == request.Sku))
            {
                throw new InvalidOperationException("DUPLICATE_SKU");
            }
        }

        if (!string.IsNullOrEmpty(request.Name)) product.Name = request.Name;
        if (!string.IsNullOrEmpty(request.Description)) product.Description = request.Description;
        if (request.Price.HasValue) product.Price = request.Price.Value;
        if (!string.IsNullOrEmpty(request.Category)) product.Category = request.Category;
        if (request.StockQuantity.HasValue) product.StockQuantity = request.StockQuantity.Value;
        if (!string.IsNullOrEmpty(request.Sku)) product.Sku = request.Sku;
        if (request.Attributes != null) product.AttributesJson = JsonSerializer.Serialize(request.Attributes);
        if (request.Images != null)
        {
            _context.ProductImages.RemoveRange(product.Images);
            foreach (var imageDto in request.Images)
            {
                product.Images.Add(new ProductImage
                {
                    Url = imageDto.Url,
                    Alt = imageDto.Alt,
                    Primary = imageDto.Primary
                });
            }
        }

        product.UpdatedAt = DateTime.UtcNow;
        product.UpdatedBy = userId;

        await _context.SaveChangesAsync();

        return MapToResponse(product);
    }

    public async Task DeleteProductAsync(Guid id)
    {
        var product = await _context.Products.FindAsync(id);
        if (product == null)
        {
            throw new KeyNotFoundException("PRODUCT_NOT_FOUND");
        }

        _context.Products.Remove(product);
        await _context.SaveChangesAsync();
    }

    public async Task<ProductResponse> UpdateProductStatusAsync(Guid id, ProductStatus status, string userId)
    {
        var product = await _context.Products.FindAsync(id);
        if (product == null)
        {
            throw new KeyNotFoundException("PRODUCT_NOT_FOUND");
        }

        product.Status = status;
        product.UpdatedAt = DateTime.UtcNow;
        product.UpdatedBy = userId;

        await _context.SaveChangesAsync();

        return MapToResponse(product);
    }

    private static ProductResponse MapToResponse(Product product)
    {
        return new ProductResponse
        {
            Id = product.Id,
            Name = product.Name,
            Description = product.Description,
            Price = product.Price,
            Category = product.Category,
            StockQuantity = product.StockQuantity,
            Sku = product.Sku,
            Images = product.Images.Select(i => new ProductImageDto
            {
                Url = i.Url,
                Alt = i.Alt,
                Primary = i.Primary
            }).ToList(),
            Attributes = JsonSerializer.Deserialize<Dictionary<string, string>>(product.AttributesJson) ?? new(),
            Status = product.Status.ToString(),
            CreatedAt = product.CreatedAt,
            UpdatedAt = product.UpdatedAt,
            CreatedBy = product.CreatedBy,
            UpdatedBy = product.UpdatedBy
        };
    }
}