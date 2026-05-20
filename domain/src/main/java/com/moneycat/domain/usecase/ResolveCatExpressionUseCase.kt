package com.moneycat.domain.usecase

import com.moneycat.domain.model.CatExpression
import com.moneycat.domain.model.MonthlySummary
import java.math.BigDecimal
import javax.inject.Inject

class ResolveCatExpressionUseCase @Inject constructor() {
    operator fun invoke(summary: MonthlySummary): CatExpression {
        val budgetLimit = summary.budgetLimit
        if (budgetLimit != null && summary.totalExpense > budgetLimit) {
            return CatExpression.ALERT
        }
        if (summary.daysSinceLastTransaction >= 7) {
            return CatExpression.SLEEPING
        }
        val previousExpense = summary.previousMonthExpense
        if (previousExpense != null && previousExpense > BigDecimal.ZERO) {
            val reduction = (previousExpense - summary.totalExpense)
                .divide(previousExpense, 2, java.math.RoundingMode.HALF_UP)
            if (reduction >= BigDecimal("0.10")) {
                return CatExpression.HAPPY
            }
        }
        return CatExpression.DEFAULT
    }
}
