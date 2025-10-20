package ECommerceFlowApi.product.model;

import java.math.BigDecimal;

import ECommerceFlowApi.common.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false, unique = true)
    private String sku;

    @Column(nullable = false, columnDefinition = "integer default 0")
    @Builder.Default
    private Integer stockQuantity = 0;

    @Column(nullable = false)
    private String category;

    private String brand;

    private String imageUrl;

    @Column(nullable = false, columnDefinition = "boolean default true")
    @Builder.Default
    private boolean active = true;

}
