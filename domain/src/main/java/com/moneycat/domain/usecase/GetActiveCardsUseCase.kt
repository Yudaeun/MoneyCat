package com.moneycat.domain.usecase

import com.moneycat.domain.repository.CardRepository
import javax.inject.Inject

class GetActiveCardsUseCase @Inject constructor(
    private val repository: CardRepository,
) {
    operator fun invoke() = repository.getActiveCards()
}
