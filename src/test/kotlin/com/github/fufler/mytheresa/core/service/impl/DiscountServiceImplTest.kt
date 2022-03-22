package com.github.fufler.mytheresa.core.service.impl

import com.github.fufler.mytheresa.core.model.Product
import com.github.fufler.mytheresa.core.service.impl.DiscountServiceImpl.Companion.SPECIFIC_CATEGORY
import com.github.fufler.mytheresa.core.service.impl.DiscountServiceImpl.Companion.SPECIFIC_SKU
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class DiscountServiceImplTest {

    private val discountServiceImpl = DiscountServiceImpl()

    private fun makeMockProduct(
        name: String? = null,
        sku: String? = null,
        category: String? = null,
        price: Int? = null
    ): Product {
        val mock = mockk<Product>()

        name?.let { every { mock.name } returns name }
        sku?.let { every { mock.sku } returns sku }
        category?.let { every { mock.category } returns category }
        price?.let { every { mock.price } returns price }

        return mock
    }

    @Test
    fun `discount is applied for specific SKU`() {
        Assertions.assertEquals(
            85,
            discountServiceImpl.getPriceWithDiscount(
                makeMockProduct(
                    sku = SPECIFIC_SKU,
                    category = "some category",
                    price = 100
                )
            )
        )
    }

    @Test
    fun `discount is applied for specific category`() {
        Assertions.assertEquals(
            700,
            discountServiceImpl.getPriceWithDiscount(
                makeMockProduct(
                    sku = "some sku",
                    category = SPECIFIC_CATEGORY,
                    price = 1000
                )
            )
        )
    }

    @Test
    fun `max discount is applied for products with both specific category and specific sku`() {
        Assertions.assertEquals(
            140,
            discountServiceImpl.getPriceWithDiscount(
                makeMockProduct(
                    sku = SPECIFIC_SKU,
                    category = SPECIFIC_CATEGORY,
                    price = 200
                )
            )
        )
    }

    @Test
    fun `no discount is applied for products that do not belong to specific category and do not have specific sku`() {
        Assertions.assertEquals(
            42,
            discountServiceImpl.getPriceWithDiscount(
                makeMockProduct(
                    sku = "some random sku",
                    category = "some random category",
                    price = 42
                )
            )
        )
    }
}