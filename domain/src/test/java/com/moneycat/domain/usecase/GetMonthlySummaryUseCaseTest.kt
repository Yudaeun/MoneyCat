package com.moneycat.domain.usecase

import app.cash.turbine.test
import com.moneycat.domain.model.Budget
import com.moneycat.domain.model.PaymentMethod
import com.moneycat.domain.model.Transaction
import com.moneycat.domain.model.TransactionType
import com.moneycat.domain.repository.BudgetRepository
import com.moneycat.domain.repository.TransactionRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

class GetMonthlySummaryUseCaseTest {

    private val transactionRepo = mockk<TransactionRepository>()
    private val budgetRepo = mockk<BudgetRepository>()
    private lateinit var useCase: GetMonthlySummaryUseCase

    @Before
    fun setup() {
        useCase = GetMonthlySummaryUseCase(transactionRepo, budgetRepo)
    }

    @Test
    fun `correctly separates income and expense totals`() = runTest {
        val ym = YearMonth.of(2026, 5)
        every { transactionRepo.getByDateRange(any(), any()) } returns flowOf(
            listOf(
                tx(TransactionType.INCOME, "500000"),
                tx(TransactionType.EXPENSE, "80000"),
                tx(TransactionType.EXPENSE, "30000"),
            )
        )
        every { budgetRepo.getByMonth(any()) } returns flowOf(emptyList())

        useCase(ym).test {
            val summary = awaitItem()
            assertEquals(BigDecimal("500000"), summary.totalIncome)
            assertEquals(BigDecimal("110000"), summary.totalExpense)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `budget limit is sum of all category limits`() = runTest {
        val ym = YearMonth.of(2026, 5)
        every { transactionRepo.getByDateRange(any(), any()) } returns flowOf(emptyList())
        every { budgetRepo.getByMonth(any()) } returns flowOf(
            listOf(
                Budget(category = "식비", yearMonth = ym.toString(), limitAmount = BigDecimal("200000")),
                Budget(category = "교통", yearMonth = ym.toString(), limitAmount = BigDecimal("50000")),
            )
        )

        useCase(ym).test {
            val summary = awaitItem()
            assertEquals(BigDecimal("250000"), summary.budgetLimit)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `budget limit is null when no budgets set`() = runTest {
        val ym = YearMonth.of(2026, 5)
        every { transactionRepo.getByDateRange(any(), any()) } returns flowOf(emptyList())
        every { budgetRepo.getByMonth(any()) } returns flowOf(emptyList())

        useCase(ym).test {
            val summary = awaitItem()
            assertNull(summary.budgetLimit)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `returns zero income and expense when no transactions`() = runTest {
        val ym = YearMonth.of(2026, 5)
        every { transactionRepo.getByDateRange(any(), any()) } returns flowOf(emptyList())
        every { budgetRepo.getByMonth(any()) } returns flowOf(emptyList())

        useCase(ym).test {
            val summary = awaitItem()
            assertEquals(BigDecimal.ZERO, summary.totalIncome)
            assertEquals(BigDecimal.ZERO, summary.totalExpense)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun tx(type: TransactionType, amount: String) = Transaction(
        type = type,
        amount = BigDecimal(amount),
        category = "식비",
        description = null,
        date = LocalDate.of(2026, 5, 10),
        paymentMethod = PaymentMethod.CARD,
        createdAt = LocalDateTime.of(2026, 5, 10, 12, 0),
    )
}
