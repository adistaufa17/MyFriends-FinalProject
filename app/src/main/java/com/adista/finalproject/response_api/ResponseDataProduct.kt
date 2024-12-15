package com.adista.finalproject.response_api

import com.adista.finalproject.data.DataProduct
import com.crocodic.core.api.ModelResponse
import com.google.gson.annotations.SerializedName

data class ResponseDataProduct(
    @SerializedName("products")
    val products: List<DataProduct>
) : ModelResponse()
