package com.moneycat.data.local.db.mapper

import com.moneycat.data.local.db.entity.AiInsightEntity
import com.moneycat.data.local.db.entity.AssetEntity
import com.moneycat.data.local.db.entity.BudgetEntity
import com.moneycat.data.local.db.entity.CardBenefitEntity
import com.moneycat.data.local.db.entity.CardEntity
import com.moneycat.data.local.db.entity.ExchangeRateEntity
import com.moneycat.data.local.db.entity.TransactionEntity
import com.moneycat.data.local.db.entity.UserProfileEntity
import com.moneycat.data.local.db.relation.CardWithBenefits
import com.moneycat.domain.model.AiInsight
import com.moneycat.domain.model.Asset
import com.moneycat.domain.model.Budget
import com.moneycat.domain.model.Card
import com.moneycat.domain.model.CardBenefit
import com.moneycat.domain.model.CardWithBenefitsDomain
import com.moneycat.domain.model.ExchangeRate
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

fun CardEntity.toDomain() = Card(
    id = id,
    name = name,
    bankName = bankName,
    type = type,
    lastFourDigits = lastFourDigits,
    isActive = isActive,
    annualFee = annualFee,
    createdAt = createdAt,
)

fun Card.toEntity() = CardEntity(
    id = id,
    name = name,
    bankName = bankName,
    type = type,
    lastFourDigits = lastFourDigits,
    isActive = isActive,
    annualFee = annualFee,
    createdAt = createdAt,
)

fun CardBenefitEntity.toDomain() = CardBenefit(
    id = id,
    cardId = cardId,
    category = category,
    benefitType = benefitType,
    discountRate = discountRate,
    monthlyLimit = monthlyLimit,
    description = description,
)

fun CardBenefit.toEntity() = CardBenefitEntity(
    id = id,
    cardId = cardId,
    category = category,
    benefitType = benefitType,
    discountRate = discountRate,
    monthlyLimit = monthlyLimit,
    description = description,
)

fun CardWithBenefits.toDomain() = CardWithBenefitsDomain(
    card = card.toDomain(),
    benefits = benefits.map { it.toDomain() },
)

fun ExchangeRateEntity.toDomain() = ExchangeRate(
    currencyPair = currencyPair,
    rate = rate,
    source = source,
    updatedAt = updatedAt,
)

fun ExchangeRate.toEntity() = ExchangeRateEntity(
    currencyPair = currencyPair,
    rate = rate,
    source = source,
    updatedAt = updatedAt,
)
