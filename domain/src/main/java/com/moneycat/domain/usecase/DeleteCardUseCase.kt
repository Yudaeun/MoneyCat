package com.moneycat.domain.usecase

import com.moneycat.domain.repository.CardRepository
import javax.inject.Inject

class DeleteCardUseCase @Inject constructor(
    private val repository: CardRepository,
) {
    suspend operator fun invoke(cardId: Long) = repository.softDelete(cardId)
}
