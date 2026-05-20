package com.moneycat.data.repository

import com.moneycat.data.local.db.dao.ExchangeRateDao
import com.moneycat.data.local.db.mapper.toDomain
import com.moneycat.data.local.db.mapper.toEntity
import com.moneycat.data.remote.ExchangeRateApiService
import com.moneycat.domain.model.ExchangeRate
import com.moneycat.domain.repository.ExchangeRateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import java.math.MathContext
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExchangeRateRepositoryImpl @Inject constructor(
    private val dao: ExchangeRateDao,
    private val apiService: ExchangeRateApiService,
) : ExchangeRateRepository {

    override fun getAll(): Flow<List<ExchangeRate>> =
        dao.getAll().map { list -> list.map { it.toDomain() } }

    override suspend fun getByPair(currencyPair: String): ExchangeRate? =
        dao.getByCurrencyPair(currencyPair)?.toDomain()

    override suspend fun refresh(): Result<Unit> = runCatching {
        val response = apiService.getRates("KRW")
        val now = LocalDateTime.now()
        listOf("USD", "JPY", "EUR").forEach { code ->
            val krwPerUnit = response.rates[code] ?: return@forEach
            // API: 1 KRW = krwPerUnit CODE → 역수: 1 CODE = ? KRW
            val rateKrw = BigDecimal.ONE.divide(
                BigDecimal(krwPerUnit.toString()),
                4,
                java.math.RoundingMode.HALF_UP,
            )
            dao.upsert(
                ExchangeRate(
                    currencyPair = "$code/KRW",
                    rate = rateKrw,
                    source = "open.er-api.com",
                    updatedAt = now,
                ).toEntity()
            )
        }
    }
}
