package com.day.moneycat.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneycat.domain.model.UserProfile
import com.moneycat.domain.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

data class ProfileEditUiState(
    val name: String = "",
    val monthlyIncome: String = "",
    val financialGoal: String = "",
    val isLoading: Boolean = true,
    val isSaved: Boolean = false,
)

@HiltViewModel
class ProfileEditViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileEditUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userProfileRepository.get().first()?.let { profile ->
                _uiState.update {
                    it.copy(
                        name = profile.name,
                        monthlyIncome = if (profile.monthlyIncome > BigDecimal.ZERO)
                            profile.monthlyIncome.toPlainString() else "",
                        financialGoal = profile.financialGoal ?: "",
                        isLoading = false,
                    )
                }
            } ?: _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun setName(value: String) = _uiState.update { it.copy(name = value) }
    fun setMonthlyIncome(value: String) = _uiState.update { it.copy(monthlyIncome = value) }
    fun setFinancialGoal(value: String) = _uiState.update { it.copy(financialGoal = value) }

    fun save() {
        val state = _uiState.value
        if (state.name.isBlank()) return
        viewModelScope.launch {
            val existing = userProfileRepository.get().first()
            userProfileRepository.upsert(
                UserProfile(
                    id = existing?.id ?: 1,
                    name = state.name.trim(),
                    monthlyIncome = state.monthlyIncome.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                    mainBankName = existing?.mainBankName,
                    financialGoal = state.financialGoal.trim().ifBlank { null },
                    onboardingCompleted = true,
                    createdAt = existing?.createdAt ?: LocalDate.now(),
                )
            )
            _uiState.update { it.copy(isSaved = true) }
        }
    }
}
