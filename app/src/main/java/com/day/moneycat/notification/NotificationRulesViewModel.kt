package com.day.moneycat.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneycat.domain.model.NotificationRule
import com.moneycat.domain.usecase.GetNotificationRulesUseCase
import com.moneycat.domain.usecase.ToggleNotificationRuleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NotificationRulesUiState(
    val rules: List<NotificationRule> = emptyList(),
)

@HiltViewModel
class NotificationRulesViewModel @Inject constructor(
    getNotificationRulesUseCase: GetNotificationRulesUseCase,
    private val toggleNotificationRuleUseCase: ToggleNotificationRuleUseCase,
) : ViewModel() {

    val uiState = getNotificationRulesUseCase()
        .map { rules -> NotificationRulesUiState(rules = rules) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = NotificationRulesUiState(),
        )

    fun toggle(id: Long, isEnabled: Boolean) = viewModelScope.launch {
        toggleNotificationRuleUseCase(id, isEnabled)
    }
}
