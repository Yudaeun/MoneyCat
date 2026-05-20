package com.moneycat.domain.usecase

import com.moneycat.domain.model.Transaction
import com.moneycat.domain.repository.TransactionRepository
import javax.inject.Inject

class UpdateTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository,
) {
    suspend operator fun invoke(transaction: Transaction) = repository.update(transaction)
}
