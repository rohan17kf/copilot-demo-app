using System.ComponentModel.DataAnnotations;
using backend_dotnet.Models;

namespace backend_dotnet.DTOs;

public class UpdateStatusRequest
{
    [Required]
    public ProductStatus Status { get; set; }
}
