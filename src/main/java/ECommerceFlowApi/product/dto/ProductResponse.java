package ECommerceFlowApi.product.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String sku;
    private Integer stockQuantity;
    private String category;
    private String brand;
    private String imageUrl;
    private boolean active;
    private LocalDateTime createdAt;
}
