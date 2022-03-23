package com.github.fufler.mytheresa.core.service.impl.model

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

/**
 * JPA repository for [DBBackedCategory]
 */
interface CategoriesRepository : JpaRepository<DBBackedCategory, Long> {
    /**
     * Returns true if there is at least one category and false otherwise
     */
    @Query("SELECT EXISTS (SELECT NULL FROM categories)", nativeQuery = true)
    fun isNotEmpty(): Boolean
}