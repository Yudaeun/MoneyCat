package com.moneycat.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class ExchangeRate(
    val currencyPair: String,
    val rate: BigDecimal,
    val source: String,
    val updatedAt: LocalDateTime,
)
