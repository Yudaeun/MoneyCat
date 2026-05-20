package com.moneycat.domain.usecase

import com.moneycat.domain.model.Card
import com.moneycat.domain.model.CardBenefit
import com.moneycat.domain.repository.CardRepository
import javax.inject.Inject

class UpdateCardUseCase @Inject constructor(
    private val repository: CardRepository,
) {
    suspend operator fun invoke(card: Card, benefits: List<CardBenefit> = emptyList()) =
        repository.update(card, benefits)
}
