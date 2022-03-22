package com.github.fufler.mytheresa.core.service

import com.github.fufler.mytheresa.api.remote.ListProductsResponse

interface ProductsService {
    fun findProducts(
        category: String? = null,
        priceLessThan: Int? = null,
        limit: Int? = null
    ): List<ListProductsResponse.Product>
}