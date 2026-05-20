package com.moneycat.domain.usecase

import com.moneycat.domain.model.ExchangeRate
import com.moneycat.domain.repository.ExchangeRateRepository
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class RefreshExchangeRatesUseCase @Inject constructor(
    private val repository: ExchangeRateRepository,
) {
    suspend operator fun invoke(existingRates: List<ExchangeRate>): Result<Unit> {
        val oldest = existingRates.minOfOrNull { it.updatedAt }
        val shouldRefresh = oldest == null ||
            ChronoUnit.HOURS.between(oldest, LocalDateTime.now()) >= 24
        return if (shouldRefresh) repository.refresh() else Result.success(Unit)
    }
}
