package com.moneycat.domain.repository

import com.moneycat.domain.model.CategorySummary
import com.moneycat.domain.model.Transaction
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface TransactionRepository {
    fun getAll(): Flow<List<Transaction>>
    fun getByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<Transaction>>
    fun getMonthlyCategoryTotals(startDate: LocalDate, endDate: LocalDate): Flow<List<CategorySummary>>
    suspend fun getById(id: Long): Transaction?
    suspend fun insert(transaction: Transaction): Long
    suspend fun update(transaction: Transaction)
    suspend fun insertAll(transactions: List<Transaction>)
    suspend fun delete(transaction: Transaction)
    suspend fun existsByHash(hash: String): Boolean
}
