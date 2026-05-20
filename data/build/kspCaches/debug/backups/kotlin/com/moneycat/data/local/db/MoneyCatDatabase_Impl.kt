package com.moneycat.`data`.local.db

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.moneycat.`data`.local.db.dao.AiInsightDao
import com.moneycat.`data`.local.db.dao.AiInsightDao_Impl
import com.moneycat.`data`.local.db.dao.AssetDao
import com.moneycat.`data`.local.db.dao.AssetDao_Impl
import com.moneycat.`data`.local.db.dao.BudgetDao
import com.moneycat.`data`.local.db.dao.BudgetDao_Impl
import com.moneycat.`data`.local.db.dao.CardBenefitDao
import com.moneycat.`data`.local.db.dao.CardBenefitDao_Impl
import com.moneycat.`data`.local.db.dao.CardDao
import com.moneycat.`data`.local.db.dao.CardDao_Impl
import com.moneycat.`data`.local.db.dao.ExchangeRateDao
import com.moneycat.`data`.local.db.dao.ExchangeRateDao_Impl
import com.moneycat.`data`.local.db.dao.NotificationRuleDao
import com.moneycat.`data`.local.db.dao.NotificationRuleDao_Impl
import com.moneycat.`data`.local.db.dao.TransactionDao
import com.moneycat.`data`.local.db.dao.TransactionDao_Impl
import com.moneycat.`data`.local.db.dao.UserProfileDao
import com.moneycat.`data`.local.db.dao.UserProfileDao_Impl
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class MoneyCatDatabase_Impl : MoneyCatDatabase() {
  private val _transactionDao: Lazy<TransactionDao> = lazy {
    TransactionDao_Impl(this)
  }

  private val _assetDao: Lazy<AssetDao> = lazy {
    AssetDao_Impl(this)
  }

  private val _cardDao: Lazy<CardDao> = lazy {
    CardDao_Impl(this)
  }

  private val _cardBenefitDao: Lazy<CardBenefitDao> = lazy {
    CardBenefitDao_Impl(this)
  }

  private val _budgetDao: Lazy<BudgetDao> = lazy {
    BudgetDao_Impl(this)
  }

  private val _exchangeRateDao: Lazy<ExchangeRateDao> = lazy {
    ExchangeRateDao_Impl(this)
  }

  private val _aiInsightDao: Lazy<AiInsightDao> = lazy {
    AiInsightDao_Impl(this)
  }

  private val _userProfileDao: Lazy<UserProfileDao> = lazy {
    UserProfileDao_Impl(this)
  }

  private val _notificationRuleDao: Lazy<NotificationRuleDao> = lazy {
    NotificationRuleDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(1,
        "4189c97022e3e5044d08d14caa6c3600", "09ed1d5166c10b66738919df9ef60566") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `user_profiles` (`id` INTEGER NOT NULL, `name` TEXT NOT NULL, `monthlyIncome` TEXT NOT NULL, `mainBankName` TEXT, `financialGoal` TEXT, `onboardingCompleted` INTEGER NOT NULL, `createdAt` TEXT NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `assets` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `type` TEXT NOT NULL, `currency` TEXT NOT NULL, `balance` TEXT NOT NULL, `description` TEXT, `updatedAt` TEXT NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `transactions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `type` TEXT NOT NULL, `amount` TEXT NOT NULL, `currency` TEXT NOT NULL, `category` TEXT NOT NULL, `description` TEXT, `date` TEXT NOT NULL, `paymentMethod` TEXT NOT NULL, `cardId` INTEGER, `source` TEXT NOT NULL, `dedupHash` TEXT, `merchantName` TEXT, `createdAt` TEXT NOT NULL, FOREIGN KEY(`cardId`) REFERENCES `cards`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_date` ON `transactions` (`date`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_cardId` ON `transactions` (`cardId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_category` ON `transactions` (`category`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `cards` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `bankName` TEXT NOT NULL, `type` TEXT NOT NULL, `lastFourDigits` TEXT, `isActive` INTEGER NOT NULL, `annualFee` TEXT, `createdAt` TEXT NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `card_benefits` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `cardId` INTEGER NOT NULL, `category` TEXT NOT NULL, `benefitType` TEXT NOT NULL, `discountRate` TEXT, `monthlyLimit` TEXT, `description` TEXT NOT NULL, FOREIGN KEY(`cardId`) REFERENCES `cards`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_card_benefits_cardId` ON `card_benefits` (`cardId`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `budgets` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `category` TEXT NOT NULL, `yearMonth` TEXT NOT NULL, `limitAmount` TEXT NOT NULL, `alertThreshold` REAL NOT NULL)")
        connection.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_budgets_category_yearMonth` ON `budgets` (`category`, `yearMonth`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `exchange_rates` (`currencyPair` TEXT NOT NULL, `rate` TEXT NOT NULL, `source` TEXT NOT NULL, `updatedAt` TEXT NOT NULL, PRIMARY KEY(`currencyPair`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `ai_insights` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `type` TEXT NOT NULL, `title` TEXT NOT NULL, `content` TEXT NOT NULL, `estimatedSaving` TEXT, `isRead` INTEGER NOT NULL, `createdAt` TEXT NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `notification_rules` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `bankName` TEXT NOT NULL, `packageName` TEXT NOT NULL, `titlePattern` TEXT NOT NULL, `bodyPattern` TEXT NOT NULL, `amountGroup` INTEGER NOT NULL, `merchantGroup` INTEGER, `isEnabled` INTEGER NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '4189c97022e3e5044d08d14caa6c3600')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `user_profiles`")
        connection.execSQL("DROP TABLE IF EXISTS `assets`")
        connection.execSQL("DROP TABLE IF EXISTS `transactions`")
        connection.execSQL("DROP TABLE IF EXISTS `cards`")
        connection.execSQL("DROP TABLE IF EXISTS `card_benefits`")
        connection.execSQL("DROP TABLE IF EXISTS `budgets`")
        connection.execSQL("DROP TABLE IF EXISTS `exchange_rates`")
        connection.execSQL("DROP TABLE IF EXISTS `ai_insights`")
        connection.execSQL("DROP TABLE IF EXISTS `notification_rules`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        connection.execSQL("PRAGMA foreign_keys = ON")
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection):
          RoomOpenDelegate.ValidationResult {
        val _columnsUserProfiles: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsUserProfiles.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfiles.put("name", TableInfo.Column("name", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfiles.put("monthlyIncome", TableInfo.Column("monthlyIncome", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfiles.put("mainBankName", TableInfo.Column("mainBankName", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfiles.put("financialGoal", TableInfo.Column("financialGoal", "TEXT", false,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfiles.put("onboardingCompleted", TableInfo.Column("onboardingCompleted",
            "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfiles.put("createdAt", TableInfo.Column("createdAt", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysUserProfiles: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesUserProfiles: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoUserProfiles: TableInfo = TableInfo("user_profiles", _columnsUserProfiles,
            _foreignKeysUserProfiles, _indicesUserProfiles)
        val _existingUserProfiles: TableInfo = read(connection, "user_profiles")
        if (!_infoUserProfiles.equals(_existingUserProfiles)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |user_profiles(com.moneycat.data.local.db.entity.UserProfileEntity).
              | Expected:
              |""".trimMargin() + _infoUserProfiles + """
              |
              | Found:
              |""".trimMargin() + _existingUserProfiles)
        }
        val _columnsAssets: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsAssets.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAssets.put("name", TableInfo.Column("name", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAssets.put("type", TableInfo.Column("type", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAssets.put("currency", TableInfo.Column("currency", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAssets.put("balance", TableInfo.Column("balance", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAssets.put("description", TableInfo.Column("description", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAssets.put("updatedAt", TableInfo.Column("updatedAt", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysAssets: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesAssets: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoAssets: TableInfo = TableInfo("assets", _columnsAssets, _foreignKeysAssets,
            _indicesAssets)
        val _existingAssets: TableInfo = read(connection, "assets")
        if (!_infoAssets.equals(_existingAssets)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |assets(com.moneycat.data.local.db.entity.AssetEntity).
              | Expected:
              |""".trimMargin() + _infoAssets + """
              |
              | Found:
              |""".trimMargin() + _existingAssets)
        }
        val _columnsTransactions: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsTransactions.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactions.put("type", TableInfo.Column("type", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactions.put("amount", TableInfo.Column("amount", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactions.put("currency", TableInfo.Column("currency", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactions.put("category", TableInfo.Column("category", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactions.put("description", TableInfo.Column("description", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactions.put("date", TableInfo.Column("date", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactions.put("paymentMethod", TableInfo.Column("paymentMethod", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactions.put("cardId", TableInfo.Column("cardId", "INTEGER", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactions.put("source", TableInfo.Column("source", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactions.put("dedupHash", TableInfo.Column("dedupHash", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactions.put("merchantName", TableInfo.Column("merchantName", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactions.put("createdAt", TableInfo.Column("createdAt", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysTransactions: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysTransactions.add(TableInfo.ForeignKey("cards", "SET NULL", "NO ACTION",
            listOf("cardId"), listOf("id")))
        val _indicesTransactions: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesTransactions.add(TableInfo.Index("index_transactions_date", false, listOf("date"),
            listOf("ASC")))
        _indicesTransactions.add(TableInfo.Index("index_transactions_cardId", false,
            listOf("cardId"), listOf("ASC")))
        _indicesTransactions.add(TableInfo.Index("index_transactions_category", false,
            listOf("category"), listOf("ASC")))
        val _infoTransactions: TableInfo = TableInfo("transactions", _columnsTransactions,
            _foreignKeysTransactions, _indicesTransactions)
        val _existingTransactions: TableInfo = read(connection, "transactions")
        if (!_infoTransactions.equals(_existingTransactions)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |transactions(com.moneycat.data.local.db.entity.TransactionEntity).
              | Expected:
              |""".trimMargin() + _infoTransactions + """
              |
              | Found:
              |""".trimMargin() + _existingTransactions)
        }
        val _columnsCards: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsCards.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCards.put("name", TableInfo.Column("name", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCards.put("bankName", TableInfo.Column("bankName", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCards.put("type", TableInfo.Column("type", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCards.put("lastFourDigits", TableInfo.Column("lastFourDigits", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCards.put("isActive", TableInfo.Column("isActive", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCards.put("annualFee", TableInfo.Column("annualFee", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCards.put("createdAt", TableInfo.Column("createdAt", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysCards: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesCards: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoCards: TableInfo = TableInfo("cards", _columnsCards, _foreignKeysCards,
            _indicesCards)
        val _existingCards: TableInfo = read(connection, "cards")
        if (!_infoCards.equals(_existingCards)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |cards(com.moneycat.data.local.db.entity.CardEntity).
              | Expected:
              |""".trimMargin() + _infoCards + """
              |
              | Found:
              |""".trimMargin() + _existingCards)
        }
        val _columnsCardBenefits: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsCardBenefits.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCardBenefits.put("cardId", TableInfo.Column("cardId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCardBenefits.put("category", TableInfo.Column("category", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCardBenefits.put("benefitType", TableInfo.Column("benefitType", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCardBenefits.put("discountRate", TableInfo.Column("discountRate", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCardBenefits.put("monthlyLimit", TableInfo.Column("monthlyLimit", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCardBenefits.put("description", TableInfo.Column("description", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysCardBenefits: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysCardBenefits.add(TableInfo.ForeignKey("cards", "CASCADE", "NO ACTION",
            listOf("cardId"), listOf("id")))
        val _indicesCardBenefits: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesCardBenefits.add(TableInfo.Index("index_card_benefits_cardId", false,
            listOf("cardId"), listOf("ASC")))
        val _infoCardBenefits: TableInfo = TableInfo("card_benefits", _columnsCardBenefits,
            _foreignKeysCardBenefits, _indicesCardBenefits)
        val _existingCardBenefits: TableInfo = read(connection, "card_benefits")
        if (!_infoCardBenefits.equals(_existingCardBenefits)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |card_benefits(com.moneycat.data.local.db.entity.CardBenefitEntity).
              | Expected:
              |""".trimMargin() + _infoCardBenefits + """
              |
              | Found:
              |""".trimMargin() + _existingCardBenefits)
        }
        val _columnsBudgets: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsBudgets.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsBudgets.put("category", TableInfo.Column("category", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsBudgets.put("yearMonth", TableInfo.Column("yearMonth", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsBudgets.put("limitAmount", TableInfo.Column("limitAmount", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsBudgets.put("alertThreshold", TableInfo.Column("alertThreshold", "REAL", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysBudgets: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesBudgets: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesBudgets.add(TableInfo.Index("index_budgets_category_yearMonth", true,
            listOf("category", "yearMonth"), listOf("ASC", "ASC")))
        val _infoBudgets: TableInfo = TableInfo("budgets", _columnsBudgets, _foreignKeysBudgets,
            _indicesBudgets)
        val _existingBudgets: TableInfo = read(connection, "budgets")
        if (!_infoBudgets.equals(_existingBudgets)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |budgets(com.moneycat.data.local.db.entity.BudgetEntity).
              | Expected:
              |""".trimMargin() + _infoBudgets + """
              |
              | Found:
              |""".trimMargin() + _existingBudgets)
        }
        val _columnsExchangeRates: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsExchangeRates.put("currencyPair", TableInfo.Column("currencyPair", "TEXT", true, 1,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsExchangeRates.put("rate", TableInfo.Column("rate", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsExchangeRates.put("source", TableInfo.Column("source", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsExchangeRates.put("updatedAt", TableInfo.Column("updatedAt", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysExchangeRates: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesExchangeRates: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoExchangeRates: TableInfo = TableInfo("exchange_rates", _columnsExchangeRates,
            _foreignKeysExchangeRates, _indicesExchangeRates)
        val _existingExchangeRates: TableInfo = read(connection, "exchange_rates")
        if (!_infoExchangeRates.equals(_existingExchangeRates)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |exchange_rates(com.moneycat.data.local.db.entity.ExchangeRateEntity).
              | Expected:
              |""".trimMargin() + _infoExchangeRates + """
              |
              | Found:
              |""".trimMargin() + _existingExchangeRates)
        }
        val _columnsAiInsights: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsAiInsights.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAiInsights.put("type", TableInfo.Column("type", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAiInsights.put("title", TableInfo.Column("title", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAiInsights.put("content", TableInfo.Column("content", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAiInsights.put("estimatedSaving", TableInfo.Column("estimatedSaving", "TEXT", false,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAiInsights.put("isRead", TableInfo.Column("isRead", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAiInsights.put("createdAt", TableInfo.Column("createdAt", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysAiInsights: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesAiInsights: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoAiInsights: TableInfo = TableInfo("ai_insights", _columnsAiInsights,
            _foreignKeysAiInsights, _indicesAiInsights)
        val _existingAiInsights: TableInfo = read(connection, "ai_insights")
        if (!_infoAiInsights.equals(_existingAiInsights)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |ai_insights(com.moneycat.data.local.db.entity.AiInsightEntity).
              | Expected:
              |""".trimMargin() + _infoAiInsights + """
              |
              | Found:
              |""".trimMargin() + _existingAiInsights)
        }
        val _columnsNotificationRules: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsNotificationRules.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsNotificationRules.put("bankName", TableInfo.Column("bankName", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsNotificationRules.put("packageName", TableInfo.Column("packageName", "TEXT", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsNotificationRules.put("titlePattern", TableInfo.Column("titlePattern", "TEXT", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsNotificationRules.put("bodyPattern", TableInfo.Column("bodyPattern", "TEXT", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsNotificationRules.put("amountGroup", TableInfo.Column("amountGroup", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsNotificationRules.put("merchantGroup", TableInfo.Column("merchantGroup", "INTEGER",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsNotificationRules.put("isEnabled", TableInfo.Column("isEnabled", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysNotificationRules: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesNotificationRules: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoNotificationRules: TableInfo = TableInfo("notification_rules",
            _columnsNotificationRules, _foreignKeysNotificationRules, _indicesNotificationRules)
        val _existingNotificationRules: TableInfo = read(connection, "notification_rules")
        if (!_infoNotificationRules.equals(_existingNotificationRules)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |notification_rules(com.moneycat.data.local.db.entity.NotificationRuleEntity).
              | Expected:
              |""".trimMargin() + _infoNotificationRules + """
              |
              | Found:
              |""".trimMargin() + _existingNotificationRules)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "user_profiles", "assets",
        "transactions", "cards", "card_benefits", "budgets", "exchange_rates", "ai_insights",
        "notification_rules")
  }

  public override fun clearAllTables() {
    super.performClear(true, "user_profiles", "assets", "transactions", "cards", "card_benefits",
        "budgets", "exchange_rates", "ai_insights", "notification_rules")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(TransactionDao::class, TransactionDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(AssetDao::class, AssetDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(CardDao::class, CardDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(CardBenefitDao::class, CardBenefitDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(BudgetDao::class, BudgetDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(ExchangeRateDao::class, ExchangeRateDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(AiInsightDao::class, AiInsightDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(UserProfileDao::class, UserProfileDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(NotificationRuleDao::class,
        NotificationRuleDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override
      fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>):
      List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun transactionDao(): TransactionDao = _transactionDao.value

  public override fun assetDao(): AssetDao = _assetDao.value

  public override fun cardDao(): CardDao = _cardDao.value

  public override fun cardBenefitDao(): CardBenefitDao = _cardBenefitDao.value

  public override fun budgetDao(): BudgetDao = _budgetDao.value

  public override fun exchangeRateDao(): ExchangeRateDao = _exchangeRateDao.value

  public override fun aiInsightDao(): AiInsightDao = _aiInsightDao.value

  public override fun userProfileDao(): UserProfileDao = _userProfileDao.value

  public override fun notificationRuleDao(): NotificationRuleDao = _notificationRuleDao.value
}
