package com.day.moneycat.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneycat.domain.model.AiInsight
import com.moneycat.domain.model.CatExpression
import com.moneycat.domain.model.Transaction
import com.moneycat.domain.repository.AiInsightRepository
import com.moneycat.domain.repository.AssetRepository
import com.moneycat.domain.repository.TransactionRepository
import com.moneycat.domain.repository.UserProfileRepository
import com.moneycat.domain.usecase.GetMonthlySummaryUseCase
import com.moneycat.domain.usecase.ResolveCatExpressionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.math.BigDecimal
import java.time.YearMonth
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = true,
    val userName: String = "",
    val totalIncome: BigDecimal = BigDecimal.ZERO,
    val totalExpense: BigDecimal = BigDecimal.ZERO,
    val budgetLimit: BigDecimal? = null,
    val totalAssets: BigDecimal = BigDecimal.ZERO,
    val recentTransactions: List<Transaction> = emptyList(),
    val catExpression: CatExpression = CatExpression.DEFAULT,
    val latestInsight: AiInsight? = null,
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    getMonthlySummaryUseCase: GetMonthlySummaryUseCase,
    private val resolveCatExpressionUseCase: ResolveCatExpressionUseCase,
    assetRepository: AssetRepository,
    transactionRepository: TransactionRepository,
    userProfileRepository: UserProfileRepository,
    aiInsightRepository: AiInsightRepository,
) : ViewModel() {

    val uiState = combine(
        getMonthlySummaryUseCase(YearMonth.now()),
        assetRepository.getAll(),
        transactionRepository.getAll(),
        userProfileRepository.get(),
        aiInsightRepository.getUnread(),
    ) { summary, assets, transactions, profile, unreadInsights ->
        HomeUiState(
            isLoading = false,
            userName = profile?.name ?: "",
            totalIncome = summary.totalIncome,
            totalExpense = summary.totalExpense,
            budgetLimit = summary.budgetLimit,
            totalAssets = assets.fold(BigDecimal.ZERO) { acc, a -> acc + a.balance },
            recentTransactions = transactions.take(5),
            catExpression = resolveCatExpressionUseCase(summary),
            latestInsight = unreadInsights.firstOrNull(),
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState(),
    )
}
