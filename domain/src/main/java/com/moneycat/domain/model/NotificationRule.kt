package com.moneycat.domain.model

data class NotificationRule(
    val id: Long = 0,
    val bankName: String,
    val packageName: String,
    val isEnabled: Boolean = true,
)
