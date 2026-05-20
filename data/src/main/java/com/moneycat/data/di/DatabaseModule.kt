package com.moneycat.data.di

import android.content.Context
import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.moneycat.data.local.db.MoneyCatDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SupportFactory
import java.security.SecureRandom
import java.util.Base64
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MoneyCatDatabase {
        val passphrase = getOrCreatePassphrase(context)
        return Room.databaseBuilder(context, MoneyCatDatabase::class.java, "moneycat.db")
            .openHelperFactory(SupportFactory(passphrase))
            .build()
    }

    private fun getOrCreatePassphrase(context: Context): ByteArray {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        val prefs = EncryptedSharedPreferences.create(
            context,
            "moneycat_db_key",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        val key = "db_passphrase_v1"
        val stored = prefs.getString(key, null)
        if (stored != null) return Base64.getDecoder().decode(stored)
        val newPassphrase = ByteArray(32).also { SecureRandom().nextBytes(it) }
        prefs.edit().putString(key, Base64.getEncoder().encodeToString(newPassphrase)).apply()
        return newPassphrase
    }

    @Provides fun provideTransactionDao(db: MoneyCatDatabase) = db.transactionDao()
    @Provides fun provideAssetDao(db: MoneyCatDatabase) = db.assetDao()
    @Provides fun provideCardDao(db: MoneyCatDatabase) = db.cardDao()
    @Provides fun provideCardBenefitDao(db: MoneyCatDatabase) = db.cardBenefitDao()
    @Provides fun provideBudgetDao(db: MoneyCatDatabase) = db.budgetDao()
    @Provides fun provideExchangeRateDao(db: MoneyCatDatabase) = db.exchangeRateDao()
    @Provides fun provideAiInsightDao(db: MoneyCatDatabase) = db.aiInsightDao()
    @Provides fun provideUserProfileDao(db: MoneyCatDatabase) = db.userProfileDao()
    @Provides fun provideNotificationRuleDao(db: MoneyCatDatabase) = db.notificationRuleDao()
}
