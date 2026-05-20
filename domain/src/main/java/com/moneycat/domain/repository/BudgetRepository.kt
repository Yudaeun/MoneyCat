package com.moneycat.domain.repository

import com.moneycat.domain.model.Budget
import kotlinx.coroutines.flow.Flow

interface BudgetRepository {
    fun getByMonth(yearMonth: String): Flow<List<Budget>>
    suspend fun upsert(budget: Budget)
    suspend fun delete(budget: Budget)
}
