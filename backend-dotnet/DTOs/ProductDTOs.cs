using System.ComponentModel.DataAnnotations;

namespace backend_dotnet.DTOs;

public class ProductImageDto
{
    public string Url { get; set; } = string.Empty;
    public string Alt { get; set; } = string.Empty;
    public bool Primary { get; set; }
}

public class CreateProductRequest
{
    [Required]
    [StringLength(100, MinimumLength = 1)]
    public string Name { get; set; } = string.Empty;

    [StringLength(500)]
    public string Description { get; set; } = string.Empty;

    [Required]
    [Range(0.01, double.MaxValue)]
    public decimal Price { get; set; }

    [Required]
    [StringLength(50, MinimumLength = 1)]
    public string Category { get; set; } = string.Empty;

    [Range(0, int.MaxValue)]
    public int StockQuantity { get; set; }

    [Required]
    [StringLength(50)]
    public string Sku { get; set; } = string.Empty;

    public List<ProductImageDto> Images { get; set; } = new();

    public Dictionary<string, string> Attributes { get; set; } = new();
}

public class UpdateProductRequest
{
    [StringLength(100, MinimumLength = 1)]
    public string? Name { get; set; }

    [StringLength(500)]
    public string? Description { get; set; }

    [Range(0.01, double.MaxValue)]
    public decimal? Price { get; set; }

    [StringLength(50, MinimumLength = 1)]
    public string? Category { get; set; }

    [Range(0, int.MaxValue)]
    public int? StockQuantity { get; set; }

    [StringLength(50)]
    public string? Sku { get; set; }

    public List<ProductImageDto>? Images { get; set; }

    public Dictionary<string, string>? Attributes { get; set; }
}

public class ProductResponse
{
    public Guid Id { get; set; }
    public string Name { get; set; } = string.Empty;
    public string Description { get; set; } = string.Empty;
    public decimal Price { get; set; }
    public string Category { get; set; } = string.Empty;
    public int StockQuantity { get; set; }
    public string Sku { get; set; } = string.Empty;
    public List<ProductImageDto> Images { get; set; } = new();
    public Dictionary<string, string> Attributes { get; set; } = new();
    public string Status { get; set; } = string.Empty;
    public DateTime CreatedAt { get; set; }
    public DateTime UpdatedAt { get; set; }
    public string CreatedBy { get; set; } = string.Empty;
    public string UpdatedBy { get; set; } = string.Empty;
}

public class PaginatedResponse<T>
{
    public List<T> Data { get; set; } = new();
    public PaginationInfo Pagination { get; set; } = new();
    public ResponseMetadata Metadata { get; set; } = new();
}

public class PaginationInfo
{
    public int Page { get; set; }
    public int PageSize { get; set; }
    public int TotalCount { get; set; }
    public int TotalPages { get; set; }
}

public class ResponseMetadata
{
    public bool Cached { get; set; }
    public string CacheAge { get; set; } = string.Empty;
    public string Source { get; set; } = string.Empty;
}

public class ErrorResponse
{
    public string Error { get; set; } = string.Empty;
    public string Code { get; set; } = string.Empty;
    public Dictionary<string, List<string>> Details { get; set; } = new();
    public DateTime Timestamp { get; set; }
    public string Path { get; set; } = string.Empty;
}