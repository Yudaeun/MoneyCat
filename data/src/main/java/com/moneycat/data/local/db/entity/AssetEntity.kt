package com.moneycat.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.moneycat.domain.model.AssetType
import com.moneycat.domain.model.Currency
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(tableName = "assets")
data class AssetEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val type: AssetType,
    val currency: Currency = Currency.KRW,
    val balance: BigDecimal,
    val description: String? = null,
    val updatedAt: LocalDateTime
)
