package com.moneycat.data.local.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.moneycat.domain.model.Currency
import com.moneycat.domain.model.InputSource
import com.moneycat.domain.model.PaymentMethod
import com.moneycat.domain.model.TransactionType
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = CardEntity::class,
            parentColumns = ["id"],
            childColumns = ["cardId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["date"]),
        Index(value = ["cardId"]),
        Index(value = ["category"])
    ]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
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
