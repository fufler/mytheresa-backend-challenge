package com.github.fufler.mytheresa.core.service

import com.github.fufler.mytheresa.core.model.Product

/**
 * Service to query for available products
 */
interface ProductsService {
    /**
     * Returns list of products with category matching [categoryName] and price <= [priceLessThan] (optionally).
     * Optional parameter may be used to [limit] max number of products to be returned.
     *
     */
    fun findProducts(
        categoryName: String,
        priceLessThan: Int? = null,
        limit: Int? = null
    ): List<Product>
}