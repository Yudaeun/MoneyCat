package com.moneycat.domain.usecase

import com.moneycat.domain.model.Card
import com.moneycat.domain.model.CardBenefit
import com.moneycat.domain.repository.CardRepository
import javax.inject.Inject

class AddCardUseCase @Inject constructor(
    private val repository: CardRepository,
) {
    suspend operator fun invoke(card: Card, benefits: List<CardBenefit> = emptyList()): Long =
        repository.insert(card, benefits)
}
