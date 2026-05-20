package com.moneycat.data.repository

import com.moneycat.data.local.db.dao.AiInsightDao
import com.moneycat.data.local.db.mapper.toDomain
import com.moneycat.data.local.db.mapper.toEntity
import com.moneycat.domain.model.AiInsight
import com.moneycat.domain.repository.AiInsightRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AiInsightRepositoryImpl @Inject constructor(
    private val dao: AiInsightDao,
) : AiInsightRepository {
    override fun getAll() = dao.getAll().map { list -> list.map { it.toDomain() } }
    override fun getUnread() = dao.getUnread().map { list -> list.map { it.toDomain() } }
    override suspend fun insert(insight: AiInsight) = dao.insert(insight.toEntity())
    override suspend fun markAsRead(id: Long) = dao.markAsRead(id)
}
