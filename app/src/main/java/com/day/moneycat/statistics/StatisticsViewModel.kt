package com.day.moneycat.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneycat.domain.model.CategorySummary
import com.moneycat.domain.model.TransactionType
import com.moneycat.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.math.BigDecimal
import java.time.YearMonth
import javax.inject.Inject

data class StatisticsUiState(
    val yearMonth: YearMonth = YearMonth.now(),
    val expenseCategories: List<CategorySummary> = emptyList(),
    val totalIncome: BigDecimal = BigDecimal.ZERO,
    val totalExpense: BigDecimal = BigDecimal.ZERO,
    val isLoading: Boolean = true,
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
) : ViewModel() {

    private val _yearMonth = MutableStateFlow(YearMonth.now())

    val uiState = _yearMonth.flatMapLatest { ym ->
        combine(
            transactionRepository.getMonthlyCategoryTotals(ym.atDay(1), ym.atEndOfMonth()),
            transactionRepository.getByDateRange(ym.atDay(1), ym.atEndOfMonth()),
        ) { categories, transactions ->
            val totalIncome = transactions
                .filter { it.type == TransactionType.INCOME }
                .fold(BigDecimal.ZERO) { acc, t -> acc + t.amount }
            val totalExpense = transactions
                .filter { it.type == TransactionType.EXPENSE }
                .fold(BigDecimal.ZERO) { acc, t -> acc + t.amount }
            StatisticsUiState(
                yearMonth = ym,
                expenseCategories = categories.sortedByDescending { it.total },
                totalIncome = totalIncome,
                totalExpense = totalExpense,
                isLoading = false,
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = StatisticsUiState(),
    )

    fun prevMonth() = _yearMonth.update { it.minusMonths(1) }
    fun nextMonth() = _yearMonth.update { it.plusMonths(1) }
}
