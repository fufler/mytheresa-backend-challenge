package com.github.fufler.mytheresa.core.service

import com.github.fufler.mytheresa.core.model.Product

interface ProductsService {
    fun findProducts(
        categoryName: String,
        priceLessThan: Int? = null,
        limit: Int? = null
    ): List<Product>
}