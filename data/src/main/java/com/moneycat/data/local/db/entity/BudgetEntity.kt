package com.moneycat.data.local.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(
    tableName = "budgets",
    indices = [Index(value = ["category", "yearMonth"], unique = true)]
)
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val category: String,
    val yearMonth: String,       // "YYYY-MM" 형식으로 저장
    val limitAmount: BigDecimal,
    val alertThreshold: Float = 0.8f
)
