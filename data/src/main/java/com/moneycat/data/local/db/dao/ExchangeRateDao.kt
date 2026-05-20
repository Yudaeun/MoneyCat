package com.moneycat.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.moneycat.data.local.db.entity.ExchangeRateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExchangeRateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(rate: ExchangeRateEntity)

    @Query("SELECT * FROM exchange_rates WHERE currencyPair = :pair LIMIT 1")
    suspend fun getByCurrencyPair(pair: String): ExchangeRateEntity?

    @Query("SELECT * FROM exchange_rates ORDER BY currencyPair")
    fun getAll(): Flow<List<ExchangeRateEntity>>
}
