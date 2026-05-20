package com.moneycat.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.moneycat.data.local.db.entity.CardEntity
import com.moneycat.data.local.db.relation.CardWithBenefits
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(card: CardEntity): Long

    @Update
    suspend fun update(card: CardEntity)

    @Query("SELECT * FROM cards WHERE isActive = 1 ORDER BY bankName, name")
    fun getActiveCards(): Flow<List<CardEntity>>

    @Transaction
    @Query("SELECT * FROM cards WHERE id = :cardId")
    fun getCardWithBenefits(cardId: Long): Flow<CardWithBenefits?>

    @Query("UPDATE cards SET isActive = 0 WHERE id = :cardId")
    suspend fun softDelete(cardId: Long)
}
