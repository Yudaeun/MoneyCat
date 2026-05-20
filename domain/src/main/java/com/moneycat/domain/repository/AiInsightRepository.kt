package com.moneycat.domain.repository

import com.moneycat.domain.model.AiInsight
import kotlinx.coroutines.flow.Flow

interface AiInsightRepository {
    fun getAll(): Flow<List<AiInsight>>
    fun getUnread(): Flow<List<AiInsight>>
    suspend fun insert(insight: AiInsight)
    suspend fun markAsRead(id: Long)
}
