package com.github.fufler.mytheresa.core.service.impl.model

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CategoriesRepository : JpaRepository<DBBackedCategory, Long> {
    @Query("SELECT EXISTS (SELECT NULL FROM categories)", nativeQuery = true)
    fun isNotEmpty(): Boolean
}