package com.github.fufler.mytheresa.api.remote

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "List products response")
data class ListProductsResponse(
    @Schema(description = "List of products matching specified criteria")
    val products: List<Product>
) {
    @Schema(description = "Single product details")
    data class Product(
        @Schema(description = "Product SKU")
        val sku: String,

        @Schema(description = "Product name")
        val name: String,

        @Schema(description = "Product category")
        val category: String,

        @Schema(description = "Product price")
        val price: Price
    ) {
        @Schema(description = "Price details")
        data class Price(
            @Schema(description = "Original price (without discount)")
            val original: Int,

            @Schema(description = "Final price (with discount applied)")
            val final: Int,

            @Schema(description = "Discount percentage. Null if no discount applied.", required = false, nullable = true)
            val discount_percentage: String?,

            @Schema(description = "Price currency")
            val currency: String
        )
    }
}