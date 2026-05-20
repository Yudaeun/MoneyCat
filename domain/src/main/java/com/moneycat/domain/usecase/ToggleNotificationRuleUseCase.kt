package com.moneycat.domain.usecase

import com.moneycat.domain.repository.NotificationRuleRepository
import javax.inject.Inject

class ToggleNotificationRuleUseCase @Inject constructor(
    private val repository: NotificationRuleRepository,
) {
    suspend operator fun invoke(id: Long, isEnabled: Boolean) =
        repository.setEnabled(id, isEnabled)
}
