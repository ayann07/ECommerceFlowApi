package ECommerceFlowApi.product.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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
    public ProductResponse createProduct(ProductRequest productRequest) {

        Product product = modelMapper.map(productRequest, Product.class);
        User user = authenticationService.getUserFromJwt();
        product.setCreatedBy(user);
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductResponse.class);
    }

    @Override
    public Page<ProductResponse> getAllProducts(ProductSearchCriteria criteria, Pageable pageable) {
        Specification<Product> spec = ProductSpecification.build(criteria);
        Page<Product> productPage = productRepository.findAll(spec, pageable);

        return productPage.map(product -> modelMapper.map(product, ProductResponse.class));
    }

    @Override
    public ProductResponse getProductById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProductById'");
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateProduct'");
    }

    @Override
    public void deleteProduct(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteProduct'");
    }

}
