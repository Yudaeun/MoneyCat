package com.moneycat.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.moneycat.data.local.db.entity.NotificationRuleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationRuleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(rules: List<NotificationRuleEntity>)

    @Query("SELECT * FROM notification_rules ORDER BY bankName")
    fun getAll(): Flow<List<NotificationRuleEntity>>

    @Query("SELECT * FROM notification_rules WHERE packageName = :packageName LIMIT 1")
    suspend fun findByPackage(packageName: String): NotificationRuleEntity?

    @Query("SELECT * FROM notification_rules WHERE isEnabled = 1")
    suspend fun getEnabled(): List<NotificationRuleEntity>
}
