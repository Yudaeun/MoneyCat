package com.moneycat.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class Asset(
    val id: Long = 0,
    val name: String,
    val type: AssetType,
    val currency: Currency = Currency.KRW,
    val balance: BigDecimal,
    val description: String? = null,
    val updatedAt: LocalDateTime
)
