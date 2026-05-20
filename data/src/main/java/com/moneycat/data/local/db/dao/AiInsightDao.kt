package com.moneycat.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.moneycat.data.local.db.entity.AiInsightEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AiInsightDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(insight: AiInsightEntity)

    @Query("SELECT * FROM ai_insights ORDER BY createdAt DESC")
    fun getAll(): Flow<List<AiInsightEntity>>

    @Query("SELECT * FROM ai_insights WHERE isRead = 0 ORDER BY createdAt DESC")
    fun getUnread(): Flow<List<AiInsightEntity>>

    @Query("UPDATE ai_insights SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: Long)

    @Query("SELECT * FROM ai_insights ORDER BY createdAt DESC LIMIT 1")
    fun getLatest(): Flow<AiInsightEntity?>
}
