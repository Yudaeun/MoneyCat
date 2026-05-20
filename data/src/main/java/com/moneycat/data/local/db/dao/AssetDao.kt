package com.moneycat.data.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.moneycat.data.local.db.entity.AssetEntity
import com.moneycat.data.local.db.model.AssetTypeTotal
import kotlinx.coroutines.flow.Flow

@Dao
interface AssetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(asset: AssetEntity): Long

    @Update
    suspend fun update(asset: AssetEntity)

    @Delete
    suspend fun delete(asset: AssetEntity)

    @Query("SELECT * FROM assets ORDER BY type, name")
    fun getAll(): Flow<List<AssetEntity>>

    @Query("SELECT type, SUM(balance) as total FROM assets GROUP BY type")
    fun getTotalByType(): Flow<List<AssetTypeTotal>>
}
