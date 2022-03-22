package com.github.fufler.mytheresa.controllers

import com.github.fufler.mytheresa.api.remote.ListProductsResponse
import com.github.fufler.mytheresa.core.service.DiscountsService
import com.github.fufler.mytheresa.core.service.ProductsService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Products", description = "Products related operations")
class ProductsController(
    private val productsService: ProductsService,
    private val discountsService: DiscountsService
) {
    @GetMapping("/products/", produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(description = "Returns list of product matching specified criteria")
    fun listProducts(
        @RequestParam(required = true)
        category: String,

        @RequestParam(required = false)
        priceLessThan: Int? = null
    ): ListProductsResponse {
        val products = productsService.findProducts(category, priceLessThan, DEFAULT_PRODUCTS_LIMIT)

        val productsWithDiscountApplied = products.map { p ->
            val finalPrice = discountsService.getPriceWithDiscount(p)

            val discountPercentage = when {
                finalPrice == p.price -> null
                p.price == 0 -> 0.0 // FIXME should we really care about it?
                else -> 100.0 * (p.price - finalPrice) / p.price
            }

            ListProductsResponse.Product(
                p.sku,
                p.name,
                p.category,
                ListProductsResponse.Product.Price(
                    p.price,
                    finalPrice,
                    discountPercentage?.let { "${it.toInt()}%" },
                    DEFAULT_CURRENCY
                )
            )
        }

        return ListProductsResponse(productsWithDiscountApplied)
    }


    companion object {
        private const val DEFAULT_PRODUCTS_LIMIT = 5
        private const val DEFAULT_CURRENCY = "EUR"
    }
}