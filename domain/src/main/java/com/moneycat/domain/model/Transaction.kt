package com.moneycat.domain.model

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class Transaction(
    val id: Long = 0,
    val type: TransactionType,
    val amount: BigDecimal,
    val currency: Currency = Currency.KRW,
    val category: String,
    val description: String?,
    val date: LocalDate,
    val paymentMethod: PaymentMethod,
    val cardId: Long? = null,
    val source: InputSource = InputSource.MANUAL,
    val dedupHash: String? = null,
    val merchantName: String? = null,
    val createdAt: LocalDateTime
)
