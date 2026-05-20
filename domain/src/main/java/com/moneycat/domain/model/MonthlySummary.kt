package com.moneycat.domain.model

import java.math.BigDecimal

data class MonthlySummary(
    val totalIncome: BigDecimal,
    val totalExpense: BigDecimal,
    val budgetLimit: BigDecimal?,
    val previousMonthExpense: BigDecimal?,
    val daysSinceLastTransaction: Int
)
