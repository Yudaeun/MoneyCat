package com.moneycat.domain.repository

import com.moneycat.domain.model.Card
import com.moneycat.domain.model.CardBenefit
import com.moneycat.domain.model.CardWithBenefitsDomain
import kotlinx.coroutines.flow.Flow

interface CardRepository {
    fun getActiveCards(): Flow<List<Card>>
    fun getCardWithBenefits(cardId: Long): Flow<CardWithBenefitsDomain?>
    suspend fun insert(card: Card, benefits: List<CardBenefit> = emptyList()): Long
    suspend fun update(card: Card, benefits: List<CardBenefit> = emptyList())
    suspend fun softDelete(cardId: Long)
}
