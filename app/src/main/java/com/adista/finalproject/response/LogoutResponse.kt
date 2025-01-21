package com.adista.finalproject.response

import com.crocodic.core.api.ModelResponse
import com.google.gson.annotations.SerializedName

data class LogoutResponse(
    @SerializedName("success")
    val isLogout: Boolean
) : ModelResponse()