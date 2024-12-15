package com.adista.finalproject.repository

import com.adista.finalproject.data.DataProduct
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.flow.Flow

interface DataProductRepo {
    @SerializedName("products")
    fun getProducts(keyword: String): Flow<List<DataProduct>>
}