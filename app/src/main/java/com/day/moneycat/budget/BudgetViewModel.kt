package com.day.moneycat.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneycat.domain.model.Budget
import com.moneycat.domain.model.CategorySummary
import com.moneycat.domain.repository.BudgetRepository
import com.moneycat.domain.repository.TransactionRepository
import com.moneycat.domain.usecase.DeleteBudgetUseCase
import com.moneycat.domain.usecase.UpsertBudgetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.YearMonth
import javax.inject.Inject

data class BudgetWithSpending(
    val budget: Budget,
    val spent: BigDecimal,
) {
    val ratio: Float
        get() = if (budget.limitAmount > BigDecimal.ZERO)
            (spent.toFloat() / budget.limitAmount.toFloat()).coerceAtLeast(0f)
        else 0f
    val isOverBudget: Boolean get() = spent > budget.limitAmount
    val isNearLimit: Boolean get() = ratio >= budget.alertThreshold && !isOverBudget
}

data class BudgetUiState(
    val yearMonth: YearMonth = YearMonth.now(),
    val items: List<BudgetWithSpending> = emptyList(),
    val isLoading: Boolean = true,
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val transactionRepository: TransactionRepository,
    private val upsertBudgetUseCase: UpsertBudgetUseCase,
    private val deleteBudgetUseCase: DeleteBudgetUseCase,
) : ViewModel() {

    private val _yearMonth = MutableStateFlow(YearMonth.now())

    val uiState = _yearMonth.flatMapLatest { ym ->
        combine(
            budgetRepository.getByMonth(ym.toString()),
            transactionRepository.getMonthlyCategoryTotals(ym.atDay(1), ym.atEndOfMonth()),
        ) { budgets, categoryTotals ->
            val spendingMap: Map<String, BigDecimal> = categoryTotals.associate { it.category to it.total }
            BudgetUiState(
                yearMonth = ym,
                items = budgets.map { b ->
                    BudgetWithSpending(b, spendingMap[b.category] ?: BigDecimal.ZERO)
                }.sortedByDescending { it.ratio },
                isLoading = false,
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = BudgetUiState(),
    )

    fun prevMonth() = _yearMonth.update { it.minusMonths(1) }
    fun nextMonth() = _yearMonth.update { it.plusMonths(1) }

    fun upsert(budget: Budget) = viewModelScope.launch { upsertBudgetUseCase(budget) }
    fun delete(budget: Budget) = viewModelScope.launch { deleteBudgetUseCase(budget) }
}
