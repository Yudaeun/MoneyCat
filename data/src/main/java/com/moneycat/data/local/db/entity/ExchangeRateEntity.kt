package com.moneycat.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(tableName = "exchange_rates")
data class ExchangeRateEntity(
    @PrimaryKey val currencyPair: String,   // e.g., "USD/KRW"
    val rate: BigDecimal,
    val source: String,
    val updatedAt: LocalDateTime
)
