package com.moneycat.`data`.local.db.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.moneycat.`data`.local.db.converter.Converters
import com.moneycat.`data`.local.db.entity.TransactionEntity
import com.moneycat.`data`.local.db.model.CardSpending
import com.moneycat.`data`.local.db.model.CategoryTotal
import com.moneycat.domain.model.Currency
import com.moneycat.domain.model.InputSource
import com.moneycat.domain.model.PaymentMethod
import com.moneycat.domain.model.TransactionType
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class TransactionDao_Impl(
  __db: RoomDatabase,
) : TransactionDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfTransactionEntity: EntityInsertAdapter<TransactionEntity>

  private val __converters: Converters = Converters()

  private val __insertAdapterOfTransactionEntity_1: EntityInsertAdapter<TransactionEntity>

  private val __deleteAdapterOfTransactionEntity: EntityDeleteOrUpdateAdapter<TransactionEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfTransactionEntity = object : EntityInsertAdapter<TransactionEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `transactions` (`id`,`type`,`amount`,`currency`,`category`,`description`,`date`,`paymentMethod`,`cardId`,`source`,`dedupHash`,`merchantName`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: TransactionEntity) {
        statement.bindLong(1, entity.id)
        val _tmp: String? = __converters.fromTransactionType(entity.type)
        if (_tmp == null) {
          statement.bindNull(2)
        } else {
          statement.bindText(2, _tmp)
        }
        val _tmp_1: String? = __converters.fromBigDecimal(entity.amount)
        if (_tmp_1 == null) {
          statement.bindNull(3)
        } else {
          statement.bindText(3, _tmp_1)
        }
        val _tmp_2: String? = __converters.fromCurrency(entity.currency)
        if (_tmp_2 == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmp_2)
        }
        statement.bindText(5, entity.category)
        val _tmpDescription: String? = entity.description
        if (_tmpDescription == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpDescription)
        }
        val _tmp_3: String? = __converters.fromLocalDate(entity.date)
        if (_tmp_3 == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmp_3)
        }
        val _tmp_4: String? = __converters.fromPaymentMethod(entity.paymentMethod)
        if (_tmp_4 == null) {
          statement.bindNull(8)
        } else {
          statement.bindText(8, _tmp_4)
        }
        val _tmpCardId: Long? = entity.cardId
        if (_tmpCardId == null) {
          statement.bindNull(9)
        } else {
          statement.bindLong(9, _tmpCardId)
        }
        val _tmp_5: String? = __converters.fromInputSource(entity.source)
        if (_tmp_5 == null) {
          statement.bindNull(10)
        } else {
          statement.bindText(10, _tmp_5)
        }
        val _tmpDedupHash: String? = entity.dedupHash
        if (_tmpDedupHash == null) {
          statement.bindNull(11)
        } else {
          statement.bindText(11, _tmpDedupHash)
        }
        val _tmpMerchantName: String? = entity.merchantName
        if (_tmpMerchantName == null) {
          statement.bindNull(12)
        } else {
          statement.bindText(12, _tmpMerchantName)
        }
        val _tmp_6: String? = __converters.fromLocalDateTime(entity.createdAt)
        if (_tmp_6 == null) {
          statement.bindNull(13)
        } else {
          statement.bindText(13, _tmp_6)
        }
      }
    }
    this.__insertAdapterOfTransactionEntity_1 = object : EntityInsertAdapter<TransactionEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR IGNORE INTO `transactions` (`id`,`type`,`amount`,`currency`,`category`,`description`,`date`,`paymentMethod`,`cardId`,`source`,`dedupHash`,`merchantName`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: TransactionEntity) {
        statement.bindLong(1, entity.id)
        val _tmp: String? = __converters.fromTransactionType(entity.type)
        if (_tmp == null) {
          statement.bindNull(2)
        } else {
          statement.bindText(2, _tmp)
        }
        val _tmp_1: String? = __converters.fromBigDecimal(entity.amount)
        if (_tmp_1 == null) {
          statement.bindNull(3)
        } else {
          statement.bindText(3, _tmp_1)
        }
        val _tmp_2: String? = __converters.fromCurrency(entity.currency)
        if (_tmp_2 == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmp_2)
        }
        statement.bindText(5, entity.category)
        val _tmpDescription: String? = entity.description
        if (_tmpDescription == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpDescription)
        }
        val _tmp_3: String? = __converters.fromLocalDate(entity.date)
        if (_tmp_3 == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmp_3)
        }
        val _tmp_4: String? = __converters.fromPaymentMethod(entity.paymentMethod)
        if (_tmp_4 == null) {
          statement.bindNull(8)
        } else {
          statement.bindText(8, _tmp_4)
        }
        val _tmpCardId: Long? = entity.cardId
        if (_tmpCardId == null) {
          statement.bindNull(9)
        } else {
          statement.bindLong(9, _tmpCardId)
        }
        val _tmp_5: String? = __converters.fromInputSource(entity.source)
        if (_tmp_5 == null) {
          statement.bindNull(10)
        } else {
          statement.bindText(10, _tmp_5)
        }
        val _tmpDedupHash: String? = entity.dedupHash
        if (_tmpDedupHash == null) {
          statement.bindNull(11)
        } else {
          statement.bindText(11, _tmpDedupHash)
        }
        val _tmpMerchantName: String? = entity.merchantName
        if (_tmpMerchantName == null) {
          statement.bindNull(12)
        } else {
          statement.bindText(12, _tmpMerchantName)
        }
        val _tmp_6: String? = __converters.fromLocalDateTime(entity.createdAt)
        if (_tmp_6 == null) {
          statement.bindNull(13)
        } else {
          statement.bindText(13, _tmp_6)
        }
      }
    }
    this.__deleteAdapterOfTransactionEntity = object :
        EntityDeleteOrUpdateAdapter<TransactionEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `transactions` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: TransactionEntity) {
        statement.bindLong(1, entity.id)
      }
    }
  }

  public override suspend fun insert(transaction: TransactionEntity): Long = performSuspending(__db,
      false, true) { _connection ->
    val _result: Long = __insertAdapterOfTransactionEntity.insertAndReturnId(_connection,
        transaction)
    _result
  }

  public override suspend fun insertAll(list: List<TransactionEntity>): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfTransactionEntity_1.insert(_connection, list)
  }

  public override suspend fun delete(transaction: TransactionEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __deleteAdapterOfTransactionEntity.handle(_connection, transaction)
  }

  public override fun getAll(): Flow<List<TransactionEntity>> {
    val _sql: String = "SELECT * FROM transactions ORDER BY date DESC"
    return createFlow(__db, false, arrayOf("transactions")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfAmount: Int = getColumnIndexOrThrow(_stmt, "amount")
        val _columnIndexOfCurrency: Int = getColumnIndexOrThrow(_stmt, "currency")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfPaymentMethod: Int = getColumnIndexOrThrow(_stmt, "paymentMethod")
        val _columnIndexOfCardId: Int = getColumnIndexOrThrow(_stmt, "cardId")
        val _columnIndexOfSource: Int = getColumnIndexOrThrow(_stmt, "source")
        val _columnIndexOfDedupHash: Int = getColumnIndexOrThrow(_stmt, "dedupHash")
        val _columnIndexOfMerchantName: Int = getColumnIndexOrThrow(_stmt, "merchantName")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: MutableList<TransactionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TransactionEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpType: TransactionType
          val _tmp: String?
          if (_stmt.isNull(_columnIndexOfType)) {
            _tmp = null
          } else {
            _tmp = _stmt.getText(_columnIndexOfType)
          }
          val _tmp_1: TransactionType? = __converters.toTransactionType(_tmp)
          if (_tmp_1 == null) {
            error("Expected NON-NULL 'com.moneycat.domain.model.TransactionType', but it was NULL.")
          } else {
            _tmpType = _tmp_1
          }
          val _tmpAmount: BigDecimal
          val _tmp_2: String?
          if (_stmt.isNull(_columnIndexOfAmount)) {
            _tmp_2 = null
          } else {
            _tmp_2 = _stmt.getText(_columnIndexOfAmount)
          }
          val _tmp_3: BigDecimal? = __converters.toBigDecimal(_tmp_2)
          if (_tmp_3 == null) {
            error("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.")
          } else {
            _tmpAmount = _tmp_3
          }
          val _tmpCurrency: Currency
          val _tmp_4: String?
          if (_stmt.isNull(_columnIndexOfCurrency)) {
            _tmp_4 = null
          } else {
            _tmp_4 = _stmt.getText(_columnIndexOfCurrency)
          }
          val _tmp_5: Currency? = __converters.toCurrency(_tmp_4)
          if (_tmp_5 == null) {
            error("Expected NON-NULL 'com.moneycat.domain.model.Currency', but it was NULL.")
          } else {
            _tmpCurrency = _tmp_5
          }
          val _tmpCategory: String
          _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpDate: LocalDate
          val _tmp_6: String?
          if (_stmt.isNull(_columnIndexOfDate)) {
            _tmp_6 = null
          } else {
            _tmp_6 = _stmt.getText(_columnIndexOfDate)
          }
          val _tmp_7: LocalDate? = __converters.toLocalDate(_tmp_6)
          if (_tmp_7 == null) {
            error("Expected NON-NULL 'java.time.LocalDate', but it was NULL.")
          } else {
            _tmpDate = _tmp_7
          }
          val _tmpPaymentMethod: PaymentMethod
          val _tmp_8: String?
          if (_stmt.isNull(_columnIndexOfPaymentMethod)) {
            _tmp_8 = null
          } else {
            _tmp_8 = _stmt.getText(_columnIndexOfPaymentMethod)
          }
          val _tmp_9: PaymentMethod? = __converters.toPaymentMethod(_tmp_8)
          if (_tmp_9 == null) {
            error("Expected NON-NULL 'com.moneycat.domain.model.PaymentMethod', but it was NULL.")
          } else {
            _tmpPaymentMethod = _tmp_9
          }
          val _tmpCardId: Long?
          if (_stmt.isNull(_columnIndexOfCardId)) {
            _tmpCardId = null
          } else {
            _tmpCardId = _stmt.getLong(_columnIndexOfCardId)
          }
          val _tmpSource: InputSource
          val _tmp_10: String?
          if (_stmt.isNull(_columnIndexOfSource)) {
            _tmp_10 = null
          } else {
            _tmp_10 = _stmt.getText(_columnIndexOfSource)
          }
          val _tmp_11: InputSource? = __converters.toInputSource(_tmp_10)
          if (_tmp_11 == null) {
            error("Expected NON-NULL 'com.moneycat.domain.model.InputSource', but it was NULL.")
          } else {
            _tmpSource = _tmp_11
          }
          val _tmpDedupHash: String?
          if (_stmt.isNull(_columnIndexOfDedupHash)) {
            _tmpDedupHash = null
          } else {
            _tmpDedupHash = _stmt.getText(_columnIndexOfDedupHash)
          }
          val _tmpMerchantName: String?
          if (_stmt.isNull(_columnIndexOfMerchantName)) {
            _tmpMerchantName = null
          } else {
            _tmpMerchantName = _stmt.getText(_columnIndexOfMerchantName)
          }
          val _tmpCreatedAt: LocalDateTime
          val _tmp_12: String?
          if (_stmt.isNull(_columnIndexOfCreatedAt)) {
            _tmp_12 = null
          } else {
            _tmp_12 = _stmt.getText(_columnIndexOfCreatedAt)
          }
          val _tmp_13: LocalDateTime? = __converters.toLocalDateTime(_tmp_12)
          if (_tmp_13 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpCreatedAt = _tmp_13
          }
          _item =
              TransactionEntity(_tmpId,_tmpType,_tmpAmount,_tmpCurrency,_tmpCategory,_tmpDescription,_tmpDate,_tmpPaymentMethod,_tmpCardId,_tmpSource,_tmpDedupHash,_tmpMerchantName,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getByDateRange(startDate: String, endDate: String):
      Flow<List<TransactionEntity>> {
    val _sql: String = "SELECT * FROM transactions WHERE date BETWEEN ? AND ? ORDER BY date DESC"
    return createFlow(__db, false, arrayOf("transactions")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, startDate)
        _argIndex = 2
        _stmt.bindText(_argIndex, endDate)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfAmount: Int = getColumnIndexOrThrow(_stmt, "amount")
        val _columnIndexOfCurrency: Int = getColumnIndexOrThrow(_stmt, "currency")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfPaymentMethod: Int = getColumnIndexOrThrow(_stmt, "paymentMethod")
        val _columnIndexOfCardId: Int = getColumnIndexOrThrow(_stmt, "cardId")
        val _columnIndexOfSource: Int = getColumnIndexOrThrow(_stmt, "source")
        val _columnIndexOfDedupHash: Int = getColumnIndexOrThrow(_stmt, "dedupHash")
        val _columnIndexOfMerchantName: Int = getColumnIndexOrThrow(_stmt, "merchantName")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: MutableList<TransactionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TransactionEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpType: TransactionType
          val _tmp: String?
          if (_stmt.isNull(_columnIndexOfType)) {
            _tmp = null
          } else {
            _tmp = _stmt.getText(_columnIndexOfType)
          }
          val _tmp_1: TransactionType? = __converters.toTransactionType(_tmp)
          if (_tmp_1 == null) {
            error("Expected NON-NULL 'com.moneycat.domain.model.TransactionType', but it was NULL.")
          } else {
            _tmpType = _tmp_1
          }
          val _tmpAmount: BigDecimal
          val _tmp_2: String?
          if (_stmt.isNull(_columnIndexOfAmount)) {
            _tmp_2 = null
          } else {
            _tmp_2 = _stmt.getText(_columnIndexOfAmount)
          }
          val _tmp_3: BigDecimal? = __converters.toBigDecimal(_tmp_2)
          if (_tmp_3 == null) {
            error("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.")
          } else {
            _tmpAmount = _tmp_3
          }
          val _tmpCurrency: Currency
          val _tmp_4: String?
          if (_stmt.isNull(_columnIndexOfCurrency)) {
            _tmp_4 = null
          } else {
            _tmp_4 = _stmt.getText(_columnIndexOfCurrency)
          }
          val _tmp_5: Currency? = __converters.toCurrency(_tmp_4)
          if (_tmp_5 == null) {
            error("Expected NON-NULL 'com.moneycat.domain.model.Currency', but it was NULL.")
          } else {
            _tmpCurrency = _tmp_5
          }
          val _tmpCategory: String
          _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpDate: LocalDate
          val _tmp_6: String?
          if (_stmt.isNull(_columnIndexOfDate)) {
            _tmp_6 = null
          } else {
            _tmp_6 = _stmt.getText(_columnIndexOfDate)
          }
          val _tmp_7: LocalDate? = __converters.toLocalDate(_tmp_6)
          if (_tmp_7 == null) {
            error("Expected NON-NULL 'java.time.LocalDate', but it was NULL.")
          } else {
            _tmpDate = _tmp_7
          }
          val _tmpPaymentMethod: PaymentMethod
          val _tmp_8: String?
          if (_stmt.isNull(_columnIndexOfPaymentMethod)) {
            _tmp_8 = null
          } else {
            _tmp_8 = _stmt.getText(_columnIndexOfPaymentMethod)
          }
          val _tmp_9: PaymentMethod? = __converters.toPaymentMethod(_tmp_8)
          if (_tmp_9 == null) {
            error("Expected NON-NULL 'com.moneycat.domain.model.PaymentMethod', but it was NULL.")
          } else {
            _tmpPaymentMethod = _tmp_9
          }
          val _tmpCardId: Long?
          if (_stmt.isNull(_columnIndexOfCardId)) {
            _tmpCardId = null
          } else {
            _tmpCardId = _stmt.getLong(_columnIndexOfCardId)
          }
          val _tmpSource: InputSource
          val _tmp_10: String?
          if (_stmt.isNull(_columnIndexOfSource)) {
            _tmp_10 = null
          } else {
            _tmp_10 = _stmt.getText(_columnIndexOfSource)
          }
          val _tmp_11: InputSource? = __converters.toInputSource(_tmp_10)
          if (_tmp_11 == null) {
            error("Expected NON-NULL 'com.moneycat.domain.model.InputSource', but it was NULL.")
          } else {
            _tmpSource = _tmp_11
          }
          val _tmpDedupHash: String?
          if (_stmt.isNull(_columnIndexOfDedupHash)) {
            _tmpDedupHash = null
          } else {
            _tmpDedupHash = _stmt.getText(_columnIndexOfDedupHash)
          }
          val _tmpMerchantName: String?
          if (_stmt.isNull(_columnIndexOfMerchantName)) {
            _tmpMerchantName = null
          } else {
            _tmpMerchantName = _stmt.getText(_columnIndexOfMerchantName)
          }
          val _tmpCreatedAt: LocalDateTime
          val _tmp_12: String?
          if (_stmt.isNull(_columnIndexOfCreatedAt)) {
            _tmp_12 = null
          } else {
            _tmp_12 = _stmt.getText(_columnIndexOfCreatedAt)
          }
          val _tmp_13: LocalDateTime? = __converters.toLocalDateTime(_tmp_12)
          if (_tmp_13 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpCreatedAt = _tmp_13
          }
          _item =
              TransactionEntity(_tmpId,_tmpType,_tmpAmount,_tmpCurrency,_tmpCategory,_tmpDescription,_tmpDate,_tmpPaymentMethod,_tmpCardId,_tmpSource,_tmpDedupHash,_tmpMerchantName,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getMonthlyCategoryTotals(startDate: String, endDate: String):
      Flow<List<CategoryTotal>> {
    val _sql: String = """
        |
        |        SELECT category, SUM(amount) as total
        |        FROM transactions
        |        WHERE date BETWEEN ? AND ?
        |          AND type = 'EXPENSE'
        |        GROUP BY category
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("transactions")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, startDate)
        _argIndex = 2
        _stmt.bindText(_argIndex, endDate)
        val _columnIndexOfCategory: Int = 0
        val _columnIndexOfTotal: Int = 1
        val _result: MutableList<CategoryTotal> = mutableListOf()
        while (_stmt.step()) {
          val _item: CategoryTotal
          val _tmpCategory: String
          _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          val _tmpTotal: String
          _tmpTotal = _stmt.getText(_columnIndexOfTotal)
          _item = CategoryTotal(_tmpCategory,_tmpTotal)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getMonthlyCardSpending(startDate: String, endDate: String):
      Flow<List<CardSpending>> {
    val _sql: String = """
        |
        |        SELECT cardId, SUM(amount) as total
        |        FROM transactions
        |        WHERE date BETWEEN ? AND ?
        |          AND cardId IS NOT NULL
        |        GROUP BY cardId
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("transactions")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, startDate)
        _argIndex = 2
        _stmt.bindText(_argIndex, endDate)
        val _columnIndexOfCardId: Int = 0
        val _columnIndexOfTotal: Int = 1
        val _result: MutableList<CardSpending> = mutableListOf()
        while (_stmt.step()) {
          val _item: CardSpending
          val _tmpCardId: Long?
          if (_stmt.isNull(_columnIndexOfCardId)) {
            _tmpCardId = null
          } else {
            _tmpCardId = _stmt.getLong(_columnIndexOfCardId)
          }
          val _tmpTotal: String
          _tmpTotal = _stmt.getText(_columnIndexOfTotal)
          _item = CardSpending(_tmpCardId,_tmpTotal)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getCategoryComparison(
    thisStart: String,
    thisEnd: String,
    lastStart: String,
    lastEnd: String,
  ): Flow<List<CategoryTotal>> {
    val _sql: String = """
        |
        |        SELECT category,
        |               SUM(CASE WHEN date BETWEEN ? AND ? THEN amount ELSE 0 END) as total
        |        FROM transactions
        |        WHERE (date BETWEEN ? AND ? OR date BETWEEN ? AND ?)
        |          AND type = 'EXPENSE'
        |        GROUP BY category
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("transactions")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, thisStart)
        _argIndex = 2
        _stmt.bindText(_argIndex, thisEnd)
        _argIndex = 3
        _stmt.bindText(_argIndex, thisStart)
        _argIndex = 4
        _stmt.bindText(_argIndex, thisEnd)
        _argIndex = 5
        _stmt.bindText(_argIndex, lastStart)
        _argIndex = 6
        _stmt.bindText(_argIndex, lastEnd)
        val _columnIndexOfCategory: Int = 0
        val _columnIndexOfTotal: Int = 1
        val _result: MutableList<CategoryTotal> = mutableListOf()
        while (_stmt.step()) {
          val _item: CategoryTotal
          val _tmpCategory: String
          _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          val _tmpTotal: String
          _tmpTotal = _stmt.getText(_columnIndexOfTotal)
          _item = CategoryTotal(_tmpCategory,_tmpTotal)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun existsByHash(hash: String): Boolean {
    val _sql: String = "SELECT COUNT(*) > 0 FROM transactions WHERE dedupHash = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, hash)
        val _result: Boolean
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp != 0
        } else {
          _result = false
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
