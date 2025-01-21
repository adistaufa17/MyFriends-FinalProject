package com.adista.finalproject.api

import com.adista.finalproject.response.LogoutResponse
import com.adista.finalproject.response.UserResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiAuthService {

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): UserResponse

    @GET("auth/logout")
    suspend fun logout(): LogoutResponse

}