package com.moneycat.data.local.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.moneycat.domain.model.BenefitType
import java.math.BigDecimal

@Entity(
    tableName = "card_benefits",
    foreignKeys = [
        ForeignKey(
            entity = CardEntity::class,
            parentColumns = ["id"],
            childColumns = ["cardId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["cardId"])]
)
data class CardBenefitEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val cardId: Long,
    val category: String,
    val benefitType: BenefitType,
    val discountRate: BigDecimal? = null,
    val monthlyLimit: BigDecimal? = null,
    val description: String
)
