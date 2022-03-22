package com.github.fufler.mytheresa.controllers

import com.github.fufler.mytheresa.api.remote.ListProductsResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Products", description = "Products related operations")
class ProductsController {
    @GetMapping("/products/", produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(description = "Returns list of product matching specified criteria")
    fun listProducts(): ListProductsResponse {
        TODO()
    }
}