package com.github.fufler.mytheresa.core.service.impl

import com.github.fufler.mytheresa.core.service.ProductsService
import com.github.fufler.mytheresa.core.service.impl.model.ProductsRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class DBBackedProductsServiceImpl(
    private val productsRepository: ProductsRepository
) : ProductsService {
    override fun findProducts(categoryName: String, priceLessThan: Int?, limit: Int?) =
        productsRepository.findProducts(
            categoryName,
            priceLessThan,
            limit?.let { Pageable.ofSize(it).first() }
        )
}