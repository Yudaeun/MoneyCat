package com.moneycat.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.moneycat.data.local.db.converter.Converters
import com.moneycat.data.local.db.dao.AiInsightDao
import com.moneycat.data.local.db.dao.AssetDao
import com.moneycat.data.local.db.dao.BudgetDao
import com.moneycat.data.local.db.dao.CardBenefitDao
import com.moneycat.data.local.db.dao.CardDao
import com.moneycat.data.local.db.dao.ExchangeRateDao
import com.moneycat.data.local.db.dao.NotificationRuleDao
import com.moneycat.data.local.db.dao.TransactionDao
import com.moneycat.data.local.db.dao.UserProfileDao
import com.moneycat.data.local.db.entity.AiInsightEntity
import com.moneycat.data.local.db.entity.AssetEntity
import com.moneycat.data.local.db.entity.BudgetEntity
import com.moneycat.data.local.db.entity.CardBenefitEntity
import com.moneycat.data.local.db.entity.CardEntity
import com.moneycat.data.local.db.entity.ExchangeRateEntity
import com.moneycat.data.local.db.entity.NotificationRuleEntity
import com.moneycat.data.local.db.entity.TransactionEntity
import com.moneycat.data.local.db.entity.UserProfileEntity

@Database(
    entities = [
        UserProfileEntity::class,
        AssetEntity::class,
        TransactionEntity::class,
        CardEntity::class,
        CardBenefitEntity::class,
        BudgetEntity::class,
        ExchangeRateEntity::class,
        AiInsightEntity::class,
        NotificationRuleEntity::class,
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class MoneyCatDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun assetDao(): AssetDao
    abstract fun cardDao(): CardDao
    abstract fun cardBenefitDao(): CardBenefitDao
    abstract fun budgetDao(): BudgetDao
    abstract fun exchangeRateDao(): ExchangeRateDao
    abstract fun aiInsightDao(): AiInsightDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun notificationRuleDao(): NotificationRuleDao
}
