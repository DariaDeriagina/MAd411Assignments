package com.example.assignment_7_android.network

import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeRateApi {

    // exemple: https://open.er-api.com/v6/latest/CAD
    @GET("v6/latest/{base}")
    suspend fun getExchangeRates(@Path("base") baseCurrency: String): ExchangeRateResponse
}
