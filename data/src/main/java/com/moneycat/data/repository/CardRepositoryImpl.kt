package com.moneycat.data.repository

import com.moneycat.data.local.db.dao.CardBenefitDao
import com.moneycat.data.local.db.dao.CardDao
import com.moneycat.data.local.db.mapper.toDomain
import com.moneycat.data.local.db.mapper.toEntity
import com.moneycat.domain.model.Card
import com.moneycat.domain.model.CardBenefit
import com.moneycat.domain.model.CardWithBenefitsDomain
import com.moneycat.domain.repository.CardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardRepositoryImpl @Inject constructor(
    private val cardDao: CardDao,
    private val cardBenefitDao: CardBenefitDao,
) : CardRepository {

    override fun getActiveCards(): Flow<List<Card>> =
        cardDao.getActiveCards().map { list -> list.map { it.toDomain() } }

    override fun getCardWithBenefits(cardId: Long): Flow<CardWithBenefitsDomain?> =
        cardDao.getCardWithBenefits(cardId).map { it?.toDomain() }

    override suspend fun insert(card: Card, benefits: List<CardBenefit>): Long {
        val cardId = cardDao.insert(card.toEntity())
        if (benefits.isNotEmpty()) {
            cardBenefitDao.insertAll(benefits.map { it.toEntity().copy(cardId = cardId) })
        }
        return cardId
    }

    override suspend fun update(card: Card, benefits: List<CardBenefit>) {
        cardDao.update(card.toEntity())
        cardBenefitDao.deleteByCardId(card.id)
        if (benefits.isNotEmpty()) {
            cardBenefitDao.insertAll(benefits.map { it.toEntity().copy(cardId = card.id) })
        }
    }

    override suspend fun softDelete(cardId: Long) = cardDao.softDelete(cardId)
}
