package com.day.moneycat.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneycat.domain.model.UserProfile
import com.moneycat.domain.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

data class OnboardingUiState(
    val step: Int = 0,
    val name: String = "",
    val monthlyIncome: String = "",
    val financialGoal: String = "",
    val isLoading: Boolean = false,
) {
    val totalSteps: Int get() = 3
    val canProceed: Boolean
        get() = when (step) {
            0 -> name.isNotBlank()
            1 -> monthlyIncome.isNotBlank() && monthlyIncome.toBigDecimalOrNull()?.let { it > BigDecimal.ZERO } == true
            else -> true
        }
}

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState = _uiState.asStateFlow()

    fun setName(value: String) = _uiState.update { it.copy(name = value) }
    fun setMonthlyIncome(value: String) = _uiState.update { it.copy(monthlyIncome = value) }
    fun setFinancialGoal(value: String) = _uiState.update { it.copy(financialGoal = value) }

    fun next() = _uiState.update { it.copy(step = it.step + 1) }
    fun back() = _uiState.update { it.copy(step = (it.step - 1).coerceAtLeast(0)) }

    fun complete(onDone: () -> Unit) {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            userProfileRepository.upsert(
                UserProfile(
                    name = state.name.trim(),
                    monthlyIncome = state.monthlyIncome.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                    mainBankName = null,
                    financialGoal = state.financialGoal.trim().ifBlank { null },
                    onboardingCompleted = true,
                    createdAt = LocalDate.now(),
                )
            )
            onDone()
        }
    }
}
