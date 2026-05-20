package com.day.moneycat.insight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneycat.domain.model.AiInsight
import com.moneycat.domain.repository.AiInsightRepository
import com.moneycat.domain.usecase.GenerateMonthlyInsightUseCase
import com.moneycat.domain.usecase.GetInsightsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject

data class AiInsightUiState(
    val insights: List<AiInsight> = emptyList(),
    val isGenerating: Boolean = false,
)

@HiltViewModel
class AiInsightViewModel @Inject constructor(
    getInsightsUseCase: GetInsightsUseCase,
    private val generateMonthlyInsightUseCase: GenerateMonthlyInsightUseCase,
    private val aiInsightRepository: AiInsightRepository,
) : ViewModel() {

    private val _isGenerating = MutableStateFlow(false)

    val uiState = combine(getInsightsUseCase(), _isGenerating) { insights, isGenerating ->
        AiInsightUiState(insights = insights, isGenerating = isGenerating)
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

    fun markAsRead(id: Long) {
        viewModelScope.launch { aiInsightRepository.markAsRead(id) }
    }
}
