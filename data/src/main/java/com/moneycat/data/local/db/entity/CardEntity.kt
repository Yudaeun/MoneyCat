package com.moneycat.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.moneycat.domain.model.CardType
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(tableName = "cards")
data class CardEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val bankName: String,
    val type: CardType,
    val lastFourDigits: String?,
    val isActive: Boolean = true,
    val annualFee: BigDecimal? = null,
    val createdAt: LocalDateTime
)
