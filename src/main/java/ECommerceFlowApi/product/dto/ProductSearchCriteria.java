package ECommerceFlowApi.product.dto;

import java.math.BigDecimal;

import lombok.Data;

/**
 * A Data Transfer Object (DTO) used to hold all the optional search parameters
 * for filtering products.
 * Spring will automatically take the URL query parameters (e.g.,
 * ?name=Laptop&maxPrice=1500)
 * and create an object of this class, filling in the fields that match.
 */
@Data
public class ProductSearchCriteria {
    // These fields will hold the filter values from the client.
    // They are all optional. If a client doesn't provide one, it will be null.
    private String name;
    private String category;
    private String brand;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}
