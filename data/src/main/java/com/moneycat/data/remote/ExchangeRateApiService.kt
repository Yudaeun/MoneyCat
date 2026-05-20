package com.moneycat.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

data class ExchangeRateResponse(
    val result: String,
    val base_code: String,
    val rates: Map<String, Double>,
)

interface ExchangeRateApiService {
    @GET("v6/latest/{baseCurrency}")
    suspend fun getRates(
        @Path("baseCurrency") baseCurrency: String = "KRW",
    ): ExchangeRateResponse
}
