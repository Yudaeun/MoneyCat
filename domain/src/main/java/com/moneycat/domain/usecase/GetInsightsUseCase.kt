package com.moneycat.domain.usecase

import com.moneycat.domain.repository.AiInsightRepository
import javax.inject.Inject

class GetInsightsUseCase @Inject constructor(
    private val repository: AiInsightRepository,
) {
    operator fun invoke() = repository.getAll()
}
