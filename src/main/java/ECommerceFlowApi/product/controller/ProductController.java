package ECommerceFlowApi.product.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ECommerceFlowApi.product.dto.ProductRequest;
import ECommerceFlowApi.product.dto.ProductResponse;
import ECommerceFlowApi.product.dto.ProductSearchCriteria;
import ECommerceFlowApi.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public Page<ProductResponse> searchProducts(ProductSearchCriteria criteria,
            @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return productService.getAllProducts(criteria, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    /**
     * An endpoint for creating a new product.
     * This is a protected endpoint, accessible only by users with the 'ADMIN' role.
     * The @PreAuthorize annotation enforces this security rule.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        ProductResponse createdProduct = productService.createProduct(request);
        // Returns a 201 Created status, which is the REST standard for a successful
        // creation.
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    /**
     * An endpoint for updating an existing product.
     * This is a protected endpoint, accessible only by users with the 'ADMIN' role.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    /**
     * An endpoint for "soft-deleting" a product.
     * This is a protected endpoint, accessible only by users with the 'ADMIN' role.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        // Returns a 204 No Content status, the standard for a successful delete
        // operation with no body.
        return ResponseEntity.noContent().build();
    }

}
