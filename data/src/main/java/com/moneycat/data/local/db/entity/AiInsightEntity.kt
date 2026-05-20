package com.moneycat.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.moneycat.domain.model.InsightType
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(tableName = "ai_insights")
data class AiInsightEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: InsightType,
    val title: String,
    val content: String,
    val estimatedSaving: BigDecimal? = null,
    val isRead: Boolean = false,
    val createdAt: LocalDateTime
)
