package com.github.fufler.mytheresa.core.service.impl.model

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

/**
 * JPA repository for [DBBackedProduct]
 */
interface ProductsRepository : JpaRepository<DBBackedProduct, String> {
    /**
     * Returns true if there is at least one product and false otherwise
     */
    @Query("SELECT EXISTS (SELECT NULL FROM products)", nativeQuery = true)
    fun isNotEmpty(): Boolean

    /**
     * Returns list of products matching specified criteria
     *
     * @param categoryName Category name product should belong to
     * @param priceLessThan Optional max price of product to include into result set
     * @param pageable Paging options
     */
    @Query(
        "SELECT p FROM DBBackedProduct p JOIN DBBackedCategory c ON p.categoryId = c.id AND c.name = :categoryName AND (:priceLessThan IS NULL OR p.price <= :priceLessThan)"
    )
    fun findProducts(categoryName: String, priceLessThan: Int?, pageable: Pageable?): List<DBBackedProduct>
}