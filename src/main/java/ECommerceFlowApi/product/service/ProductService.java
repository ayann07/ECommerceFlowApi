package ECommerceFlowApi.product.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ECommerceFlowApi.product.dto.ProductRequest;
import ECommerceFlowApi.product.dto.ProductResponse;
import ECommerceFlowApi.product.dto.ProductSearchCriteria;

public interface ProductService {
    ProductResponse createProduct(ProductRequest productRequest);

    Page<ProductResponse> getAllProducts(ProductSearchCriteria criteria, Pageable pageable);

    ProductResponse getProductById(Long id);

    ProductResponse updateProduct(Long id, ProductRequest request);

    void deleteProduct(Long id);
}