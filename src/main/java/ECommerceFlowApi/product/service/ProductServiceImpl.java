package ECommerceFlowApi.product.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ECommerceFlowApi.exception.sub_exceptions.ResourceNotFoundException;
import ECommerceFlowApi.product.dto.ProductRequest;
import ECommerceFlowApi.product.dto.ProductResponse;
import ECommerceFlowApi.product.dto.ProductSearchCriteria;
import ECommerceFlowApi.product.model.Product;
import ECommerceFlowApi.product.repository.ProductRepository;
import ECommerceFlowApi.product.specification.ProductSpecification;
import ECommerceFlowApi.security.AuthenticationService;
import ECommerceFlowApi.user.model.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final AuthenticationService authenticationService;

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest) {

        Product product = modelMapper.map(productRequest, Product.class);
        User user = authenticationService.getUserFromJwt();
        product.setCreatedBy(user);
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(ProductSearchCriteria criteria, Pageable pageable) {
        Specification<Product> spec = ProductSpecification.build(criteria);
        Page<Product> productPage = productRepository.findAll(spec, pageable);

        return productPage.map(product -> modelMapper.map(product, ProductResponse.class));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id:" + id));

        // This implementation uses a clean, modern Java pattern with Optional.

        // productRepository.findById(id): This queries the database for a product with
        // the matching id. It doesn't return the Product directly; it returns an
        // Optional to safely handle the case where nothing is found.

        // .orElseThrow(...): This is a powerful method on the Optional class.

        // If the Optional contains a Product, this method unwraps it and returns the
        // Product object.

        // If the Optional is empty, it throws the exception you provide. In this case,
        // it's our ResourceNotFoundException
        return modelMapper.map(product, ProductResponse.class);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product existingProduct = findExistingProduct(id);
        updateProductFromDto(existingProduct, request);

        Product updatedProduct = productRepository.save(existingProduct);
        return modelMapper.map(updatedProduct, ProductResponse.class);

    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product existingProduct = findExistingProduct(id);
        existingProduct.setDeleted(true);
        productRepository.save(existingProduct);

    }

    private void updateProductFromDto(Product product, ProductRequest request) {

        // Only update the name if a new name was provided in the request
        if (request.getName() != null) {
            product.setName(request.getName());
        }

        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }

        if (request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }

        if (request.getSku() != null) {
            product.setSku(request.getSku());
        }

        if (request.getStockQuantity() != null) {
            product.setStockQuantity(request.getStockQuantity());
        }

        if (request.getBrand() != null) {
            product.setBrand(request.getBrand());
        }
        if (request.getCategory() != null) {
            product.setCategory(request.getCategory());
        }
    }

    private Product findExistingProduct(Long id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("product not found with id:" + id));
        return existingProduct;
    }
}
