package com.github.fufler.mytheresa.core.model

/**
 * Represents product entity
 */
interface Product {
    /**
     * Product name
     */
    val name: String

    /**
     * Product SKU
     */
    val sku: String

    /**
     * Product category
     */
    val category: String

    /**
     * Product price
     */
    val price: Int
}