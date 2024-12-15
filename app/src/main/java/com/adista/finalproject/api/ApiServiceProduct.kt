package com.adista.finalproject.api

import com.adista.finalproject.response_api.ResponseDataProduct
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServiceProduct {
    @GET("products/search")
    suspend fun getProducts(
        @Query("q") keyword: String
    ): ResponseDataProduct
}