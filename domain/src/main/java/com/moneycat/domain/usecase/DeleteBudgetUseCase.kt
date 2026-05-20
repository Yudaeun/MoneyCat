package com.moneycat.domain.usecase

import com.moneycat.domain.model.Budget
import com.moneycat.domain.repository.BudgetRepository
import javax.inject.Inject

class DeleteBudgetUseCase @Inject constructor(private val repository: BudgetRepository) {
    suspend operator fun invoke(budget: Budget) = repository.delete(budget)
}
