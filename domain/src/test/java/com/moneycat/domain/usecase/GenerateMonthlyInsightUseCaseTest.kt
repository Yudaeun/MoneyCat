package com.moneycat.domain.usecase

import com.moneycat.domain.model.AiInsight
import com.moneycat.domain.model.Budget
import com.moneycat.domain.model.InsightType
import com.moneycat.domain.model.PaymentMethod
import com.moneycat.domain.model.Transaction
import com.moneycat.domain.model.TransactionType
import com.moneycat.domain.repository.AiInsightRepository
import com.moneycat.domain.repository.BudgetRepository
import com.moneycat.domain.repository.CardRepository
import com.moneycat.domain.repository.TransactionRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

class GenerateMonthlyInsightUseCaseTest {

    private val transactionRepo = mockk<TransactionRepository>()
    private val budgetRepo = mockk<BudgetRepository>()
    private val aiInsightRepo = mockk<AiInsightRepository>()
    private val cardRepo = mockk<CardRepository>()
    private lateinit var useCase: GenerateMonthlyInsightUseCase

    @Before
    fun setup() {
        useCase = GenerateMonthlyInsightUseCase(transactionRepo, budgetRepo, aiInsightRepo, cardRepo)
        coEvery { aiInsightRepo.insert(any()) } returns Unit
        every { cardRepo.getActiveCards() } returns flowOf(emptyList())
    }

    @Test
    fun `generates SAVING_TIP and WEEKLY_SUMMARY for normal expenses`() = runTest {
        val ym = YearMonth.of(2026, 5)
        every { transactionRepo.getByDateRange(any(), any()) } returns flowOf(
            listOf(
                tx(TransactionType.EXPENSE, "100000", "식비"),
                tx(TransactionType.EXPENSE, "50000", "교통"),
                tx(TransactionType.INCOME, "1000000", "급여"),
            )
        )
        every { budgetRepo.getByMonth(any()) } returns flowOf(emptyList())

        val captured = mutableListOf<AiInsight>()
        coEvery { aiInsightRepo.insert(capture(captured)) } returns Unit

        useCase(ym)

        val types = captured.map { it.type }
        assertTrue(InsightType.SAVING_TIP in types)
        assertTrue(InsightType.WEEKLY_SUMMARY in types)
    }

    @Test
    fun `generates SPENDING_ALERT when expense reaches 90 percent of budget`() = runTest {
        val ym = YearMonth.of(2026, 5)
        every { transactionRepo.getByDateRange(any(), any()) } returns flowOf(
            listOf(tx(TransactionType.EXPENSE, "95000", "식비"))
        )
        every { budgetRepo.getByMonth(any()) } returns flowOf(
            listOf(Budget(category = "식비", yearMonth = ym.toString(), limitAmount = BigDecimal("100000")))
        )

        val captured = mutableListOf<AiInsight>()
        coEvery { aiInsightRepo.insert(capture(captured)) } returns Unit

        useCase(ym)

        assertTrue(captured.any { it.type == InsightType.SPENDING_ALERT })
    }

    @Test
    fun `does not generate SPENDING_ALERT when expense is below 90 percent of budget`() = runTest {
        val ym = YearMonth.of(2026, 5)
        every { transactionRepo.getByDateRange(any(), any()) } returns flowOf(
            listOf(tx(TransactionType.EXPENSE, "80000", "식비"))
        )
        every { budgetRepo.getByMonth(any()) } returns flowOf(
            listOf(Budget(category = "식비", yearMonth = ym.toString(), limitAmount = BigDecimal("100000")))
        )

        val captured = mutableListOf<AiInsight>()
        coEvery { aiInsightRepo.insert(capture(captured)) } returns Unit

        useCase(ym)

        assertTrue(captured.none { it.type == InsightType.SPENDING_ALERT })
    }

    @Test
    fun `generates ANOMALY when single expense is more than 2_5x the average`() = runTest {
        val ym = YearMonth.of(2026, 5)
        every { transactionRepo.getByDateRange(any(), any()) } returns flowOf(
            listOf(
                tx(TransactionType.EXPENSE, "10000", "식비"),
                tx(TransactionType.EXPENSE, "10000", "교통"),
                tx(TransactionType.EXPENSE, "300000", "의료"),
            )
        )
        every { budgetRepo.getByMonth(any()) } returns flowOf(emptyList())

        val captured = mutableListOf<AiInsight>()
        coEvery { aiInsightRepo.insert(capture(captured)) } returns Unit

        useCase(ym)

        assertTrue(captured.any { it.type == InsightType.ANOMALY })
    }

    @Test
    fun `does nothing when there are no expenses`() = runTest {
        val ym = YearMonth.of(2026, 5)
        every { transactionRepo.getByDateRange(any(), any()) } returns flowOf(emptyList())
        every { budgetRepo.getByMonth(any()) } returns flowOf(emptyList())

        useCase(ym)

        coVerify(exactly = 0) { aiInsightRepo.insert(any()) }
    }

    @Test
    fun `SAVING_TIP targets the highest spending category`() = runTest {
        val ym = YearMonth.of(2026, 5)
        every { transactionRepo.getByDateRange(any(), any()) } returns flowOf(
            listOf(
                tx(TransactionType.EXPENSE, "50000", "교통"),
                tx(TransactionType.EXPENSE, "200000", "쇼핑"),
            )
        )
        every { budgetRepo.getByMonth(any()) } returns flowOf(emptyList())

        val captured = mutableListOf<AiInsight>()
        coEvery { aiInsightRepo.insert(capture(captured)) } returns Unit

        useCase(ym)

        val tip = captured.first { it.type == InsightType.SAVING_TIP }
        assertTrue(tip.title.contains("쇼핑"))
    }

    private fun tx(type: TransactionType, amount: String, category: String) = Transaction(
        type = type,
        amount = BigDecimal(amount),
        category = category,
        description = null,
        date = LocalDate.of(2026, 5, 10),
        paymentMethod = PaymentMethod.CARD,
        createdAt = LocalDateTime.of(2026, 5, 10, 12, 0),
    )
}
