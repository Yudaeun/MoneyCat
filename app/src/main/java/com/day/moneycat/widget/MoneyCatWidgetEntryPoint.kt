package com.day.moneycat.widget

import com.moneycat.domain.repository.BudgetRepository
import com.moneycat.domain.repository.TransactionRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface MoneyCatWidgetEntryPoint {
    fun transactionRepository(): TransactionRepository
    fun budgetRepository(): BudgetRepository
}
