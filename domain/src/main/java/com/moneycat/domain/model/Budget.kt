package com.moneycat.domain.model

import java.math.BigDecimal

data class Budget(
    val id: Long = 0,
    val category: String,
    val yearMonth: String,
    val limitAmount: BigDecimal,
    val alertThreshold: Float = 0.8f
)
