package com.moneycat.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class Card(
    val id: Long = 0,
    val name: String,
    val bankName: String,
    val type: CardType,
    val lastFourDigits: String? = null,
    val isActive: Boolean = true,
    val annualFee: BigDecimal? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)

data class CardBenefit(
    val id: Long = 0,
    val cardId: Long = 0,
    val category: String,
    val benefitType: BenefitType,
    val discountRate: BigDecimal? = null,
    val monthlyLimit: BigDecimal? = null,
    val description: String,
)

data class CardWithBenefitsDomain(
    val card: Card,
    val benefits: List<CardBenefit>,
)
