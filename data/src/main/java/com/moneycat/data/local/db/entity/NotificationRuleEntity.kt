package com.moneycat.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notification_rules")
data class NotificationRuleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bankName: String,
    val packageName: String,
    val titlePattern: String,
    val bodyPattern: String,
    val amountGroup: Int,
    val merchantGroup: Int? = null,
    val isEnabled: Boolean = true
)
