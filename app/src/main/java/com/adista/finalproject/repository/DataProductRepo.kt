package com.adista.finalproject.repository

import com.adista.finalproject.data.DataProduct
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.flow.Flow

interface DataProductRepo {
    @SerializedName("products")
    fun getProducts(keyword: String): Flow<List<DataProduct>>

    fun sortProducts(sortBy: String, order: String): Flow<List<DataProduct>>

    fun filterProducts(filter: String): Flow<List<DataProduct>>
}