package com.moneycat.data.di

import com.moneycat.data.repository.AiInsightRepositoryImpl
import com.moneycat.data.repository.AssetRepositoryImpl
import com.moneycat.data.repository.BudgetRepositoryImpl
import com.moneycat.data.repository.CardRepositoryImpl
import com.moneycat.data.repository.ExchangeRateRepositoryImpl
import com.moneycat.data.repository.TransactionRepositoryImpl
import com.moneycat.data.repository.UserProfileRepositoryImpl
import com.moneycat.domain.repository.AiInsightRepository
import com.moneycat.domain.repository.AssetRepository
import com.moneycat.domain.repository.BudgetRepository
import com.moneycat.domain.repository.CardRepository
import com.moneycat.domain.repository.ExchangeRateRepository
import com.moneycat.domain.repository.TransactionRepository
import com.moneycat.domain.repository.UserProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides @Singleton
    fun provideTransactionRepository(impl: TransactionRepositoryImpl): TransactionRepository = impl

    @Provides @Singleton
    fun provideAssetRepository(impl: AssetRepositoryImpl): AssetRepository = impl

    @Provides @Singleton
    fun provideBudgetRepository(impl: BudgetRepositoryImpl): BudgetRepository = impl

    @Provides @Singleton
    fun provideUserProfileRepository(impl: UserProfileRepositoryImpl): UserProfileRepository = impl

    @Provides @Singleton
    fun provideAiInsightRepository(impl: AiInsightRepositoryImpl): AiInsightRepository = impl

    @Provides @Singleton
    fun provideCardRepository(impl: CardRepositoryImpl): CardRepository = impl

    @Provides @Singleton
    fun provideExchangeRateRepository(impl: ExchangeRateRepositoryImpl): ExchangeRateRepository = impl
}
