package com.github.fufler.mytheresa.core.service

import com.github.fufler.mytheresa.core.model.Product

/**
 * Service to calculate discounts applicable to products
 */
interface DiscountsService {
    /**
     * Returns discounted price for specified [product]
     */
    fun getPriceWithDiscount(product: Product): Int
}