package com.github.fufler.mytheresa.core.service.impl.model

import javax.persistence.*

@Entity
@Table(name = "categories")
data class DBBackedCategory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    val name: String
)