package com.github.fufler.mytheresa.core.service.impl.model

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ProductsRepository : JpaRepository<DBBackedProduct, String> {
    @Query("SELECT EXISTS (SELECT NULL FROM products)", nativeQuery = true)
    fun isNotEmpty(): Boolean

    @Query(
        "SELECT p FROM DBBackedProduct p JOIN DBBackedCategory c ON p.categoryId = c.id AND c.name = :categoryName AND (:priceLessThan IS NULL OR p.price < :priceLessThan)"
    )
    fun findProducts(categoryName: String, priceLessThan: Int?, pageable: Pageable?): List<DBBackedProduct>
}