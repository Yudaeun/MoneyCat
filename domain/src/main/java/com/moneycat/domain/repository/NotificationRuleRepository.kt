package com.moneycat.domain.repository

import com.moneycat.domain.model.NotificationRule
import kotlinx.coroutines.flow.Flow

interface NotificationRuleRepository {
    fun getAll(): Flow<List<NotificationRule>>
    suspend fun setEnabled(id: Long, isEnabled: Boolean)
}
