package com.moneycat.data.repository

import com.moneycat.data.local.db.dao.BudgetDao
import com.moneycat.data.local.db.mapper.toDomain
import com.moneycat.data.local.db.mapper.toEntity
import com.moneycat.domain.model.Budget
import com.moneycat.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BudgetRepositoryImpl @Inject constructor(
    private val dao: BudgetDao
) : BudgetRepository {

    override fun getByMonth(yearMonth: String): Flow<List<Budget>> =
        dao.getByMonth(yearMonth).map { list -> list.map { it.toDomain() } }

    override suspend fun upsert(budget: Budget) =
        dao.upsert(budget.toEntity())

    override suspend fun delete(budget: Budget) =
        dao.delete(budget.toEntity())
}
