package com.day.moneycat.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.day.moneycat.widget.WidgetUpdater
import com.moneycat.domain.model.Transaction
import com.moneycat.domain.model.TransactionType
import com.moneycat.domain.repository.TransactionRepository
import com.moneycat.domain.usecase.DeleteTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.YearMonth
import javax.inject.Inject

enum class TransactionFilter { ALL, INCOME, EXPENSE }

data class TransactionListUiState(
    val yearMonth: YearMonth = YearMonth.now(),
    val filter: TransactionFilter = TransactionFilter.ALL,
    val searchQuery: String = "",
    val transactions: List<Transaction> = emptyList(),
    val totalIncome: BigDecimal = BigDecimal.ZERO,
    val totalExpense: BigDecimal = BigDecimal.ZERO,
    val isLoading: Boolean = true,
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TransactionListViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val widgetUpdater: WidgetUpdater,
) : ViewModel() {

    private val _yearMonth = MutableStateFlow(YearMonth.now())
    private val _filter = MutableStateFlow(TransactionFilter.ALL)
    private val _searchQuery = MutableStateFlow("")

    val uiState = combine(_yearMonth, _filter, _searchQuery) { ym, filter, query ->
        Triple(ym, filter, query)
    }.flatMapLatest { (ym, filter, query) ->
        transactionRepository.getByDateRange(ym.atDay(1), ym.atEndOfMonth())
            .map { transactions ->
                val totalIncome = transactions.filter { it.type == TransactionType.INCOME }
                    .fold(BigDecimal.ZERO) { acc, t -> acc + t.amount }
                val totalExpense = transactions.filter { it.type == TransactionType.EXPENSE }
                    .fold(BigDecimal.ZERO) { acc, t -> acc + t.amount }
                val filtered = when (filter) {
                    TransactionFilter.ALL -> transactions
                    TransactionFilter.INCOME -> transactions.filter { it.type == TransactionType.INCOME }
                    TransactionFilter.EXPENSE -> transactions.filter { it.type == TransactionType.EXPENSE }
                }.let { list ->
                    if (query.isBlank()) list
                    else list.filter { tx ->
                        tx.category.contains(query, ignoreCase = true) ||
                            tx.description?.contains(query, ignoreCase = true) == true
                    }
                }
                TransactionListUiState(
                    yearMonth = ym,
                    filter = filter,
                    searchQuery = query,
                    transactions = filtered,
                    totalIncome = totalIncome,
                    totalExpense = totalExpense,
                    isLoading = false,
                )
            }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TransactionListUiState(),
    )

    fun prevMonth() = _yearMonth.update { it.minusMonths(1) }
    fun nextMonth() = _yearMonth.update { it.plusMonths(1) }
    fun setFilter(filter: TransactionFilter) = _filter.update { filter }
    fun setSearchQuery(query: String) = _searchQuery.update { query }

    fun delete(transaction: Transaction) = viewModelScope.launch {
        deleteTransactionUseCase(transaction)
        widgetUpdater.update()
    }
}
