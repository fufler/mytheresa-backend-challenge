package com.github.fufler.mytheresa.core.service.impl.model

import com.github.fufler.mytheresa.core.model.Product
import javax.persistence.*

@Entity
@Table(name = "products")
data class DBBackedProduct(
    @Id
    override val sku: String,
    override val name: String,
    override val price: Int,

    @Column(name = "category")
    val categoryId: Long,
) : Product {
    override val category: String
        get() = categoryReference.name

    @ManyToOne
    @JoinColumn(name = "category", referencedColumnName = "id", insertable = false, updatable = false)
    @get:Transient
    lateinit var categoryReference: DBBackedCategory
}