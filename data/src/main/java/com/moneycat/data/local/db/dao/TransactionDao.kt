package com.moneycat.data.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.moneycat.data.local.db.entity.TransactionEntity
import com.moneycat.data.local.db.model.CardSpending
import com.moneycat.data.local.db.model.CategoryTotal
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: TransactionEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(list: List<TransactionEntity>)

    @Delete
    suspend fun delete(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAll(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getByDateRange(startDate: String, endDate: String): Flow<List<TransactionEntity>>

    @Query("""
        SELECT category, SUM(amount) as total
        FROM transactions
        WHERE date BETWEEN :startDate AND :endDate
          AND type = 'EXPENSE'
        GROUP BY category
    """)
    fun getMonthlyCategoryTotals(startDate: String, endDate: String): Flow<List<CategoryTotal>>

    @Query("""
        SELECT cardId, SUM(amount) as total
        FROM transactions
        WHERE date BETWEEN :startDate AND :endDate
          AND cardId IS NOT NULL
        GROUP BY cardId
    """)
    fun getMonthlyCardSpending(startDate: String, endDate: String): Flow<List<CardSpending>>

    @Query("""
        SELECT category,
               SUM(CASE WHEN date BETWEEN :thisStart AND :thisEnd THEN amount ELSE 0 END) as total
        FROM transactions
        WHERE (date BETWEEN :thisStart AND :thisEnd OR date BETWEEN :lastStart AND :lastEnd)
          AND type = 'EXPENSE'
        GROUP BY category
    """)
    fun getCategoryComparison(
        thisStart: String, thisEnd: String,
        lastStart: String, lastEnd: String
    ): Flow<List<CategoryTotal>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getById(id: Long): TransactionEntity?

    @Query("SELECT COUNT(*) > 0 FROM transactions WHERE dedupHash = :hash")
    suspend fun existsByHash(hash: String): Boolean
}
