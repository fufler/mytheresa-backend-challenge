package com.github.fufler.mytheresa

import com.fasterxml.jackson.databind.JsonNode
import com.github.fufler.mytheresa.api.remote.ListProductsResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ProductsIT : AbstractIntegrationTest() {
    private fun queryProducts(category: String, priceLessThan: Int? = null) =
        performGetRequest(
            "/products/",
            listOfNotNull(
                "category" to category,
                priceLessThan?.let { "priceLessThan" to it }
            ).toMap(),
            ListProductsResponse::class
        ).products

    @Test
    fun `by category filtering should work`() {
        for (category in listOf("boots", "sandals", "sneakers")) {
            val products = queryProducts(category)

            assertTrue(products.isNotEmpty())

            products.forEach {
                assertEquals(category, it.category)
            }
        }
    }

    @Test
    fun `by price filtering should work and price filter should applied prior to discount computation`() {
        assertEquals(1, queryProducts("sneakers", 59000).size)
        assertEquals(1, queryProducts("sneakers", 59001).size)

        assertEquals(1, queryProducts("boots", 71000).size)
        assertEquals(1, queryProducts("boots", 71001).size)

        assertEquals(3, queryProducts("boots", 100000).size)
    }

    @Test
    fun `products endpoint should return no more then 5 items`() {
        val products = queryProducts("boots")

        assertEquals(5, products.size)
    }

    @Test
    fun `discounts are applied correctly`() {
        val priceBySku = queryProducts("boots", 100000)
            .associateBy(ListProductsResponse.Product::sku) { it.price }

        priceBySku.getValue("000001").apply {
            assertEquals(89000, original)
            assertEquals(62300, final)
            assertEquals("30%", discount_percentage)
        }

        priceBySku.getValue("000002").apply {
            assertEquals(99000, original)
            assertEquals(69300, final)
            assertEquals("30%", discount_percentage)
        }

        priceBySku.getValue("000003").apply {
            assertEquals(71000, original)
            assertEquals(49700, final)
            assertEquals("30%", discount_percentage)
        }

        for (category in listOf("sandals", "sneakers")) {
            val products = queryProducts(category)

            assertTrue(products.isNotEmpty())

            products.forEach {
                assertEquals(null, it.price.discount_percentage)
                assertTrue(it.price.original == it.price.final)
            }
        }
    }

    @Test
    fun `currency should always be EUR`() {
        for (category in listOf("boots", "sandals", "sneakers")) {
            val products = queryProducts(category)

            assertTrue(products.isNotEmpty())

            products.forEach {
                assertEquals("EUR", it.price.currency)
            }
        }
    }

    @Test
    fun `discount percentage should be explicitly null when missing`() {
        for (category in listOf("sandals", "sneakers")) {
            val products = performGetRequest(
                "/products/",
                mapOf("category" to category),
                JsonNode::class
            )

            val productsField = products.get("products")

            assertTrue(productsField.isArray)

            productsField.forEach {
                assertTrue(it.isObject)

                val priceField = it.get("price")

                assertTrue(priceField.isObject)
                assertTrue(priceField.has("discount_percentage"))
                assertTrue(priceField.get("discount_percentage").isNull)
            }
        }
    }
}