package com.example.assignmnet_7_android.network

import retrofit2.http.GET

interface CurrencyApiService {
    @GET("v1/latest.json")
    suspend fun getExchangeRates(): ExchangeRates
}
