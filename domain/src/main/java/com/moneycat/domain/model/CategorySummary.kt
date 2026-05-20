package com.moneycat.domain.model

import java.math.BigDecimal

data class CategorySummary(
    val category: String,
    val total: BigDecimal
)
