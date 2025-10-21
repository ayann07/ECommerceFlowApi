package ECommerceFlowApi.product.specification;

import org.springframework.data.jpa.domain.Specification;

import ECommerceFlowApi.product.dto.ProductSearchCriteria;
import ECommerceFlowApi.product.model.Product;

/**
 * A helper class that builds a dynamic JPA Specification from the search
 * criteria.
 * This keeps all the complex query-building logic in one clean, organized
 * place.
 */
public class ProductSpecification {
    /**
     * Builds a JPA Specification based on the filter criteria provided by the user.
     * 
     * @param criteria The DTO containing all the filter values.
     * @return A Specification<Product> object that represents the WHERE clause of a
     *         SQL query.
     */
    public static Specification<Product> build(ProductSearchCriteria criteria) {
        // Start with a base specification that does nothing (it will return all
        // products).

        Specification<Product> spec = Specification.unrestricted();

        // We check each filter in the criteria object. If a filter is present,
        // we add a new condition to our specification using ".and()".

        if (criteria.getName() != null && !criteria.getName().isEmpty()) {

            // This ensures we only apply the filter when the user actually passed a product
            // name to search for.
            // If name is not provided (i.e., null or empty), this condition is skipped, so
            // the query won't filter by name.

            // Add a condition to search for the product name (case-insensitive "like"
            // search).

            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("name")), "%" + criteria.getName().toLowerCase() + "%"));

            // -------------------------------------------------------------
            // spec = spec.and(...) -> Here we are chaining a new filtering condition to the
            // existing Specification.

            // spec represents the accumulated set of filtering rules so far.

            // .and(...) adds a new condition that must also be true (like SQL's AND).
            // -------------------------------------------------------------
            // (root, query, criteriaBuilder) -> { ... }

            // This is a lambda expression representing a Specification's internal
            // structure.

            // Each Specification defines how to build a single condition.
            // The three parameters come from the JPA Criteria API:
            // -------------------------------------------------------------
            // root → Represents the Entity/Table

            // The root parameter (of type Root<Product>) represents the entity being
            // queried — essentially the main table in the SQL query.

            // You use it to access the fields (or columns) of that entity.

            // For example, root.get("name") refers to the name column of the Product table.

            // -------------------------------------------------------------
            // query → Represents the Entire SQL Query

            // The query parameter (of type CriteriaQuery<?>) represents the overall
            // structure of the query being built — not just the filter part.

            // It allows you to modify things like sorting, grouping, or fetch joins if
            // needed.

            // You usually don't touch it for simple filters, but it's important when you
            // want to:

            // Remove duplicates (query.distinct(true))

            // Add sorting (query.orderBy(...))

            // Join related entities (like fetching createdBy user)
            // So, in simple words:

            // “query represents the overall SQL query being generated — including SELECT,
            // WHERE, ORDER BY, etc.”

            // -------------------------------------------------------------
            // criteriaBuilder → The Predicate Factory

            // The criteriaBuilder (of type CriteriaBuilder) is a factory object provided by
            // JPA that helps you construct the actual SQL conditions.

            // It has methods like:

            // cb.equal(...) → for equality (=)

            // cb.like(...) → for LIKE queries

            // cb.greaterThan(...) / cb.lessThan(...) → for numeric comparisons

            // cb.and(...) / cb.or(...) → for combining conditions

            // So it's like a toolbox for building type-safe SQL expressions in Java.

            // In plain English:

            // “criteriaBuilder is used to create the actual filtering conditions
            // (predicates) that go inside the WHERE clause.”

            // -------------------------------------------------------------

            // criteriaBuilder.like(
            // criteriaBuilder.lower(root.get("name")),
            // "%" + criteria.getName().toLowerCase() + "%")

            // -------------------------------------------------------------

            // root.get("name") — Access the Field from the Entity

            // root represents the entity (for example, the Product table).

            // root.get("name") means “get the column named name from this table.”

            // -------------------------------------------------------------

            // criteriaBuilder.lower(root.get("name")) — Convert It to Lowercase

            // criteriaBuilder.lower(...) converts the database column value to lowercase at
            // query time.

            // So if the product name in the DB is "Laptop" or "LAPTOP", it becomes "laptop"
            // during comparison.

            // This is important because SQL comparisons with LIKE are case-sensitive in
            // most databases.
            // -------------------------------------------------------------

            // "%" + criteria.getName().toLowerCase() + "%" — Build the Search Pattern

            // The user input (e.g., "Lap") is converted to lowercase using .toLowerCase().

            // Then % wildcards are added on both sides:

            // "%" means “match any number of characters before or after.”

            // So "Lap" becomes "%lap%".

            // -------------------------------------------------------------

            // criteriaBuilder.like(...) — Build the LIKE Predicate

            // The criteriaBuilder.like() method constructs a SQL LIKE condition between two
            // expressions.

            // In this case:

            // The first argument is the lowercased column (LOWER(name))

            // The second argument is the lowercased user input pattern (%lap%)

            // So overall, this translates to:

            // LOWER(name) LIKE '%lap%'

            // -------------------------------------------------------------

        }

        if (criteria.getCategory() != null && !criteria.getCategory().isEmpty()) {
            // Add a condition to find an exact match for the category.
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category"),
                    criteria.getCategory()));
        }
        if (criteria.getBrand() != null && !criteria.getBrand().isEmpty()) {
            spec = spec.and(
                    (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("brand"), criteria.getBrand()));
        }

        if (criteria.getMinPrice() != null) {
            // Add a "greater than or equal to" condition for the price.
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("price"),
                    criteria.getMinPrice()));
        }
        if (criteria.getMaxPrice() != null) {
            // Add a "less than or equal to" condition for the price.
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("price"),
                    criteria.getMaxPrice()));
        }

        // Return the final, combined specification.
        return spec;
    }
}
