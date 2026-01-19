using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace backend_dotnet.Models;

public enum ProductStatus
{
    ACTIVE,
    INACTIVE,
    DISCONTINUED
}

public class ProductImage
{
    [Key]
    public Guid Id { get; set; }
    public string Url { get; set; } = string.Empty;
    public string Alt { get; set; } = string.Empty;
    public bool Primary { get; set; }
    public Guid ProductId { get; set; }
    public Product? Product { get; set; }
}

public class Product
{
    [Key]
    public Guid Id { get; set; } = Guid.NewGuid();

    [Required]
    [StringLength(100, MinimumLength = 1)]
    public string Name { get; set; } = string.Empty;

    [StringLength(500)]
    public string Description { get; set; } = string.Empty;

    [Required]
    [Range(0.01, double.MaxValue)]
    [Column(TypeName = "decimal(18,2)")]
    public decimal Price { get; set; }

    [Required]
    [StringLength(50, MinimumLength = 1)]
    public string Category { get; set; } = string.Empty;

    [Range(0, int.MaxValue)]
    public int StockQuantity { get; set; }

    [Required]
    [StringLength(50)]
    public string Sku { get; set; } = string.Empty;

    public List<ProductImage> Images { get; set; } = new();

    public string AttributesJson { get; set; } = "{}";

    [Required]
    public ProductStatus Status { get; set; } = ProductStatus.ACTIVE;

    public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
    public DateTime UpdatedAt { get; set; } = DateTime.UtcNow;
    public string CreatedBy { get; set; } = string.Empty;
    public string UpdatedBy { get; set; } = string.Empty;
}