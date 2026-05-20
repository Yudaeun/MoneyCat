package com.moneycat.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDate

@Entity(tableName = "user_profiles")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String,
    val monthlyIncome: BigDecimal,
    val mainBankName: String?,
    val financialGoal: String?,
    val onboardingCompleted: Boolean = false,
    val createdAt: LocalDate
)
