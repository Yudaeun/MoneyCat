package com.moneycat.data.repository

import com.moneycat.data.local.db.dao.TransactionDao
import com.moneycat.data.local.db.mapper.toDomain
import com.moneycat.data.local.db.mapper.toEntity
import com.moneycat.domain.model.CategorySummary
import com.moneycat.domain.model.Transaction
import com.moneycat.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepositoryImpl @Inject constructor(
    private val dao: TransactionDao
) : TransactionRepository {

    override fun getAll(): Flow<List<Transaction>> =
        dao.getAll().map { list -> list.map { it.toDomain() } }

    override fun getByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<Transaction>> =
        dao.getByDateRange(startDate.toString(), endDate.toString())
            .map { list -> list.map { it.toDomain() } }

    override fun getMonthlyCategoryTotals(
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<CategorySummary>> =
        dao.getMonthlyCategoryTotals(startDate.toString(), endDate.toString())
            .map { list -> list.map { CategorySummary(it.category, BigDecimal(it.total)) } }

    override suspend fun getById(id: Long): Transaction? =
        dao.getById(id)?.toDomain()

    override suspend fun insert(transaction: Transaction): Long =
        dao.insert(transaction.toEntity())

    override suspend fun update(transaction: Transaction) {
        dao.insert(transaction.toEntity())
    }

    override suspend fun insertAll(transactions: List<Transaction>) =
        dao.insertAll(transactions.map { it.toEntity() })

    override suspend fun delete(transaction: Transaction) =
        dao.delete(transaction.toEntity())

    override suspend fun existsByHash(hash: String): Boolean =
        dao.existsByHash(hash)
}
