package com.example.assignmnet_7_android.network

import com.example.assignment_7_android.models.ExchangeRateResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeRateApi {
    @GET("v6/latest/{currency}")
    suspend fun getRates(@Path("currency") base: String): ExchangeRateResponse
}
