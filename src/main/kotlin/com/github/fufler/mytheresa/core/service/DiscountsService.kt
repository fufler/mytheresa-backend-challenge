package com.github.fufler.mytheresa.core.service

import com.github.fufler.mytheresa.core.model.Product

interface DiscountsService {
    fun getPriceWithDiscount(product: Product): Int
}