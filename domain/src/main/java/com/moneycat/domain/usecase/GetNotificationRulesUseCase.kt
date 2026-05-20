package com.moneycat.domain.usecase

import com.moneycat.domain.model.NotificationRule
import com.moneycat.domain.repository.NotificationRuleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotificationRulesUseCase @Inject constructor(
    private val repository: NotificationRuleRepository,
) {
    operator fun invoke(): Flow<List<NotificationRule>> = repository.getAll()
}
