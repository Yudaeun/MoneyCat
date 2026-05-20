package com.moneycat.domain.usecase

import com.moneycat.domain.model.MonthlySummary
import com.moneycat.domain.model.TransactionType
import com.moneycat.domain.repository.BudgetRepository
import com.moneycat.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class GetMonthlySummaryUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val budgetRepository: BudgetRepository
) {
    operator fun invoke(yearMonth: YearMonth = YearMonth.now()): Flow<MonthlySummary> {
        val start = yearMonth.atDay(1)
        val end = yearMonth.atEndOfMonth()
        val prevMonth = yearMonth.minusMonths(1)
        val prevStart = prevMonth.atDay(1)
        val prevEnd = prevMonth.atEndOfMonth()

        return combine(
            transactionRepository.getByDateRange(start, end),
            transactionRepository.getByDateRange(prevStart, prevEnd),
            budgetRepository.getByMonth(yearMonth.toString())
        ) { current, prev, budgets ->
            val totalIncome = current
                .filter { it.type == TransactionType.INCOME }
                .fold(BigDecimal.ZERO) { acc, t -> acc + t.amount }
            val totalExpense = current
                .filter { it.type == TransactionType.EXPENSE }
                .fold(BigDecimal.ZERO) { acc, t -> acc + t.amount }
            val prevExpense = prev
                .filter { it.type == TransactionType.EXPENSE }
                .fold(BigDecimal.ZERO) { acc, t -> acc + t.amount }
            val budgetLimit = budgets
                .fold(BigDecimal.ZERO) { acc, b -> acc + b.limitAmount }
                .takeIf { it > BigDecimal.ZERO }
            val lastDate = current.maxOfOrNull { it.date }
            val daysSince = if (lastDate != null)
                ChronoUnit.DAYS.between(lastDate, LocalDate.now()).toInt()
            else
                Int.MAX_VALUE

            MonthlySummary(
                totalIncome = totalIncome,
                totalExpense = totalExpense,
                budgetLimit = budgetLimit,
                previousMonthExpense = prevExpense.takeIf { it > BigDecimal.ZERO },
                daysSinceLastTransaction = daysSince
            )
        }
    }
}
