package com.moneycat.data.local.db.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.moneycat.data.local.db.entity.CardBenefitEntity
import com.moneycat.data.local.db.entity.CardEntity

data class CardWithBenefits(
    @Embedded val card: CardEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "cardId"
    )
    val benefits: List<CardBenefitEntity>
)
