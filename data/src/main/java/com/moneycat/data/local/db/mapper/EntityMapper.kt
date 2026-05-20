package com.moneycat.data.local.db.mapper

import com.moneycat.data.local.db.entity.AiInsightEntity
import com.moneycat.data.local.db.entity.AssetEntity
import com.moneycat.data.local.db.entity.BudgetEntity
import com.moneycat.data.local.db.entity.TransactionEntity
import com.moneycat.data.local.db.entity.UserProfileEntity
import com.moneycat.domain.model.AiInsight
import com.moneycat.domain.model.Asset
import com.moneycat.domain.model.Budget
import com.moneycat.domain.model.Transaction
import com.moneycat.domain.model.UserProfile

fun TransactionEntity.toDomain() = Transaction(
    id = id,
    type = type,
    amount = amount,
    currency = currency,
    category = category,
    description = description,
    date = date,
    paymentMethod = paymentMethod,
    cardId = cardId,
    source = source,
    dedupHash = dedupHash,
    merchantName = merchantName,
    createdAt = createdAt
)

fun Transaction.toEntity() = TransactionEntity(
    id = id,
    type = type,
    amount = amount,
    currency = currency,
    category = category,
    description = description,
    date = date,
    paymentMethod = paymentMethod,
    cardId = cardId,
    source = source,
    dedupHash = dedupHash,
    merchantName = merchantName,
    createdAt = createdAt
)

fun AssetEntity.toDomain() = Asset(
    id = id,
    name = name,
    type = type,
    currency = currency,
    balance = balance,
    description = description,
    updatedAt = updatedAt
)

fun Asset.toEntity() = AssetEntity(
    id = id,
    name = name,
    type = type,
    currency = currency,
    balance = balance,
    description = description,
    updatedAt = updatedAt
)

fun BudgetEntity.toDomain() = Budget(
    id = id,
    category = category,
    yearMonth = yearMonth,
    limitAmount = limitAmount,
    alertThreshold = alertThreshold
)

fun Budget.toEntity() = BudgetEntity(
    id = id,
    category = category,
    yearMonth = yearMonth,
    limitAmount = limitAmount,
    alertThreshold = alertThreshold
)

fun UserProfileEntity.toDomain() = UserProfile(
    id = id,
    name = name,
    monthlyIncome = monthlyIncome,
    mainBankName = mainBankName,
    financialGoal = financialGoal,
    onboardingCompleted = onboardingCompleted,
    createdAt = createdAt
)

fun UserProfile.toEntity() = UserProfileEntity(
    id = id,
    name = name,
    monthlyIncome = monthlyIncome,
    mainBankName = mainBankName,
    financialGoal = financialGoal,
    onboardingCompleted = onboardingCompleted,
    createdAt = createdAt
)

fun AiInsightEntity.toDomain() = AiInsight(
    id = id,
    type = type,
    title = title,
    content = content,
    estimatedSaving = estimatedSaving,
    isRead = isRead,
    createdAt = createdAt,
)

fun AiInsight.toEntity() = AiInsightEntity(
    id = id,
    type = type,
    title = title,
    content = content,
    estimatedSaving = estimatedSaving,
    isRead = isRead,
    createdAt = createdAt,
)
