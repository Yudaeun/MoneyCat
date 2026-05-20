package com.moneycat.domain.usecase

import com.moneycat.domain.model.CatExpression
import com.moneycat.domain.model.MonthlySummary
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class ResolveCatExpressionUseCaseTest {

    private val useCase = ResolveCatExpressionUseCase()

    @Test
    fun `returns ALERT when expense exceeds budget limit`() {
        assertEquals(CatExpression.ALERT, useCase(summary(expense = "150000", budget = "100000")))
    }

    @Test
    fun `returns SLEEPING when no transactions for 7 days`() {
        assertEquals(CatExpression.SLEEPING, useCase(summary(daysSince = 7)))
    }

    @Test
    fun `returns SLEEPING when no transactions for more than 7 days`() {
        assertEquals(CatExpression.SLEEPING, useCase(summary(daysSince = 14)))
    }

    @Test
    fun `returns HAPPY when expense reduced 10% from previous month`() {
        assertEquals(CatExpression.HAPPY, useCase(summary(expense = "90000", prevExpense = "100000", daysSince = 1)))
    }

    @Test
    fun `returns HAPPY when expense reduced more than 10% from previous month`() {
        assertEquals(CatExpression.HAPPY, useCase(summary(expense = "50000", prevExpense = "100000", daysSince = 1)))
    }

    @Test
    fun `returns DEFAULT when expense reduced less than 10% from previous month`() {
        assertEquals(CatExpression.DEFAULT, useCase(summary(expense = "95000", prevExpense = "100000", daysSince = 1)))
    }

    @Test
    fun `returns DEFAULT when no budget or previous month data`() {
        assertEquals(CatExpression.DEFAULT, useCase(summary(expense = "50000", daysSince = 2)))
    }

    @Test
    fun `ALERT takes priority over SLEEPING`() {
        assertEquals(
            CatExpression.ALERT,
            useCase(summary(expense = "150000", budget = "100000", daysSince = 10))
        )
    }

    private fun summary(
        income: String = "500000",
        expense: String = "50000",
        budget: String? = null,
        prevExpense: String? = null,
        daysSince: Int = 0,
    ) = MonthlySummary(
        totalIncome = BigDecimal(income),
        totalExpense = BigDecimal(expense),
        budgetLimit = budget?.let { BigDecimal(it) },
        previousMonthExpense = prevExpense?.let { BigDecimal(it) },
        daysSinceLastTransaction = daysSince,
    )
}
