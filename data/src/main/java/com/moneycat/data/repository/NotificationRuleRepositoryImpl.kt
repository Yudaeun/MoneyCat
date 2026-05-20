package com.moneycat.data.repository

import com.moneycat.data.local.db.dao.NotificationRuleDao
import com.moneycat.data.local.db.mapper.toDomain
import com.moneycat.domain.model.NotificationRule
import com.moneycat.domain.repository.NotificationRuleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRuleRepositoryImpl @Inject constructor(
    private val dao: NotificationRuleDao,
) : NotificationRuleRepository {

    override fun getAll(): Flow<List<NotificationRule>> =
        dao.getAll().map { list -> list.map { it.toDomain() } }

    override suspend fun setEnabled(id: Long, isEnabled: Boolean) =
        dao.setEnabled(id, isEnabled)
}
