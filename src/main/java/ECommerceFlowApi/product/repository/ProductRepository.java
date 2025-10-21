package ECommerceFlowApi.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import ECommerceFlowApi.product.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

}

// JpaSpecificationExecutor is an interface that adds support for dynamic,
// programmatic filtering (Specifications) on top of your normal CRUD
// operations.

// So while JpaRepository gives you:

// .findAll()

// .save()

// .deleteById()

// .findById()

// JpaSpecificationExecutor adds:

// .findAll(Specification<T> spec)

// .findAll(Specification<T> spec, Pageable pageable)

// .count(Specification<T> spec)

// .findOne(Specification<T> spec)

// These methods let you filter results dynamically using the Specification
// logic you build (like your ProductSpecification class).