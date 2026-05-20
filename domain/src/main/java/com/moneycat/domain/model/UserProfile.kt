package com.moneycat.domain.model

import java.math.BigDecimal
import java.time.LocalDate

data class UserProfile(
    val id: Int = 1,
    val name: String,
    val monthlyIncome: BigDecimal,
    val mainBankName: String?,
    val financialGoal: String?,
    val onboardingCompleted: Boolean = false,
    val createdAt: LocalDate
)
