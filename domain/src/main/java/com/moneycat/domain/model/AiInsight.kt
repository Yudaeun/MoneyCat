package com.moneycat.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class AiInsight(
    val id: Long = 0,
    val type: InsightType,
    val title: String,
    val content: String,
    val estimatedSaving: BigDecimal? = null,
    val isRead: Boolean = false,
    val createdAt: LocalDateTime,
)
