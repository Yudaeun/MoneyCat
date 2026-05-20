package com.moneycat.domain.usecase

import com.moneycat.domain.model.Transaction
import com.moneycat.domain.repository.TransactionRepository
import javax.inject.Inject

class DeleteTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction) = repository.delete(transaction)
}
