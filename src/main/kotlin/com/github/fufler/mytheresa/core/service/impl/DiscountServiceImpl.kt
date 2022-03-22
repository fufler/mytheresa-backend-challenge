package com.github.fufler.mytheresa.core.service.impl

import com.github.fufler.mytheresa.core.model.Product
import com.github.fufler.mytheresa.core.service.DiscountsService
import org.springframework.stereotype.Service
import kotlin.math.ceil

@Service
class DiscountServiceImpl : DiscountsService {
    init {
        // The following check are required only if we decide to go with externally configurable discounts mappings
        DISCOUNT_BY_CATEGORY.forEach { (category, discount) ->
            check(discount >= 0 && discount < 1.0) {
                "Invalid discount $discount for category $category: discount must be in range [0.0, 1)"
            }
        }

        DISCOUNT_BY_SKU.forEach { (sku, discount) ->
            check(discount >= 0 && discount < 1.0) {
                "Invalid discount $discount for sku $sku: discount must be in range [0.0, 1)"
            }
        }
    }

    override fun getPriceWithDiscount(product: Product): Int {
        val discount = maxOf(
            DISCOUNT_BY_CATEGORY[product.category] ?: 0.0,
            DISCOUNT_BY_SKU[product.sku] ?: 0.0
        )

        // FIXME Which round method should we use here?
        return ceil(product.price * (1.0 - discount)).toInt()
    }

    companion object {
        const val SPECIFIC_SKU = "000003"
        const val SPECIFIC_CATEGORY = "boots"

        private val DISCOUNT_BY_CATEGORY = mapOf(
            SPECIFIC_CATEGORY to 0.3
        )

        private val DISCOUNT_BY_SKU = mapOf(
            SPECIFIC_SKU to 0.15
        )
    }
}