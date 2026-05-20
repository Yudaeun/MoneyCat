package com.moneycat.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.moneycat.data.local.db.entity.CardBenefitEntity

@Dao
interface CardBenefitDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(benefits: List<CardBenefitEntity>)

    @Query("DELETE FROM card_benefits WHERE cardId = :cardId")
    suspend fun deleteByCardId(cardId: Long)
}
