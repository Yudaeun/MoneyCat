package com.day.moneycat.insight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.day.moneycat.ai.GeminiInsightService
import com.moneycat.domain.model.AiInsight
import com.moneycat.domain.model.InsightType
import com.moneycat.domain.model.TransactionType
import com.moneycat.domain.repository.AiInsightRepository
import com.moneycat.domain.repository.TransactionRepository
import com.moneycat.domain.usecase.GenerateMonthlyInsightUseCase
import com.moneycat.domain.usecase.GetInsightsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.YearMonth
import javax.inject.Inject

data class AiInsightUiState(
    val insights: List<AiInsight> = emptyList(),
    val isGenerating: Boolean = false,
    val isGeminiAnalyzing: Boolean = false,
)

@HiltViewModel
class AiInsightViewModel @Inject constructor(
    getInsightsUseCase: GetInsightsUseCase,
    private val generateMonthlyInsightUseCase: GenerateMonthlyInsightUseCase,
    private val aiInsightRepository: AiInsightRepository,
    private val geminiInsightService: GeminiInsightService,
    private val transactionRepository: TransactionRepository,
) : ViewModel() {

    private val _isGenerating = MutableStateFlow(false)
    private val _isGeminiAnalyzing = MutableStateFlow(false)

    val uiState = combine(
        getInsightsUseCase(),
        _isGenerating,
        _isGeminiAnalyzing,
    ) { insights, isGenerating, isGeminiAnalyzing ->
        AiInsightUiState(
            insights = insights,
            isGenerating = isGenerating,
            isGeminiAnalyzing = isGeminiAnalyzing,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AiInsightUiState(),
    )

    fun generate() {
        viewModelScope.launch {
            _isGenerating.value = true
            generateMonthlyInsightUseCase(YearMonth.now())
            _isGenerating.value = false
        }
    }

    fun geminiAnalyze() {
        viewModelScope.launch {
            _isGeminiAnalyzing.value = true
            val ym = YearMonth.now()
            val transactions = transactionRepository
                .getByDateRange(ym.atDay(1), ym.atEndOfMonth())
                .first()
            val expenses = transactions.filter { it.type == TransactionType.EXPENSE }

            if (expenses.isNotEmpty()) {
                val summary = expenses
                    .groupBy { it.category }
                    .mapValues { (_, txs) -> txs.fold(BigDecimal.ZERO) { acc, t -> acc + t.amount } }
                    .entries
                    .sortedByDescending { it.value }
                    .take(5)
                    .joinToString("\n") { (cat, amount) -> "- $cat: ₩${amount.toLong()}" }

                val totalExpense = expenses.fold(BigDecimal.ZERO) { acc, t -> acc + t.amount }

                val prompt = """
                    아래는 ${ym.year}년 ${ym.monthValue}월의 지출 내역입니다.
                    소비 패턴을 분석하고 절약 방법을 한국어로 간결하게 조언해주세요.

                    카테고리별 지출:
                    $summary

                    총 지출: ₩${totalExpense.toLong()}

                    3가지 구체적인 절약 팁을 짧게 제공해주세요.
                """.trimIndent()

                geminiInsightService.analyze(prompt).onSuccess { content ->
                    aiInsightRepository.insert(
                        AiInsight(
                            type = InsightType.GEMINI_ANALYSIS,
                            title = "Gemini AI 소비 분석 (${ym.monthValue}월)",
                            content = content,
                            createdAt = LocalDateTime.now(),
                        )
                    )
                }
            }
            _isGeminiAnalyzing.value = false
        }
    }

    fun markAsRead(id: Long) {
        viewModelScope.launch { aiInsightRepository.markAsRead(id) }
    }
}
