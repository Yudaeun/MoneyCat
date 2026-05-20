package com.moneycat.domain.repository

import com.moneycat.domain.model.ExchangeRate
import kotlinx.coroutines.flow.Flow

interface ExchangeRateRepository {
    fun getAll(): Flow<List<ExchangeRate>>
    suspend fun getByPair(currencyPair: String): ExchangeRate?
    suspend fun refresh(): Result<Unit>
}
