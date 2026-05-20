package com.moneycat.domain.usecase

import com.moneycat.domain.repository.ExchangeRateRepository
import javax.inject.Inject

class GetExchangeRatesUseCase @Inject constructor(
    private val repository: ExchangeRateRepository,
) {
    operator fun invoke() = repository.getAll()
}
