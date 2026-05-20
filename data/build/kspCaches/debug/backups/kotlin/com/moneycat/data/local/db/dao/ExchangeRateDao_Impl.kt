package com.moneycat.`data`.local.db.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.moneycat.`data`.local.db.converter.Converters
import com.moneycat.`data`.local.db.entity.ExchangeRateEntity
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.`annotation`.processing.Generated
import kotlin.Int
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
public class ExchangeRateDao_Impl(
  __db: RoomDatabase,
) : ExchangeRateDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfExchangeRateEntity: EntityInsertAdapter<ExchangeRateEntity>

  private val __converters: Converters = Converters()
  init {
    this.__db = __db
    this.__insertAdapterOfExchangeRateEntity = object : EntityInsertAdapter<ExchangeRateEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `exchange_rates` (`currencyPair`,`rate`,`source`,`updatedAt`) VALUES (?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: ExchangeRateEntity) {
        statement.bindText(1, entity.currencyPair)
        val _tmp: String? = __converters.fromBigDecimal(entity.rate)
        if (_tmp == null) {
          statement.bindNull(2)
        } else {
          statement.bindText(2, _tmp)
        }
        statement.bindText(3, entity.source)
        val _tmp_1: String? = __converters.fromLocalDateTime(entity.updatedAt)
        if (_tmp_1 == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmp_1)
        }
      }
    }
  }

  public override suspend fun upsert(rate: ExchangeRateEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __insertAdapterOfExchangeRateEntity.insert(_connection, rate)
  }

  public override suspend fun getByCurrencyPair(pair: String): ExchangeRateEntity? {
    val _sql: String = "SELECT * FROM exchange_rates WHERE currencyPair = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, pair)
        val _columnIndexOfCurrencyPair: Int = getColumnIndexOrThrow(_stmt, "currencyPair")
        val _columnIndexOfRate: Int = getColumnIndexOrThrow(_stmt, "rate")
        val _columnIndexOfSource: Int = getColumnIndexOrThrow(_stmt, "source")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _result: ExchangeRateEntity?
        if (_stmt.step()) {
          val _tmpCurrencyPair: String
          _tmpCurrencyPair = _stmt.getText(_columnIndexOfCurrencyPair)
          val _tmpRate: BigDecimal
          val _tmp: String?
          if (_stmt.isNull(_columnIndexOfRate)) {
            _tmp = null
          } else {
            _tmp = _stmt.getText(_columnIndexOfRate)
          }
          val _tmp_1: BigDecimal? = __converters.toBigDecimal(_tmp)
          if (_tmp_1 == null) {
            error("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.")
          } else {
            _tmpRate = _tmp_1
          }
          val _tmpSource: String
          _tmpSource = _stmt.getText(_columnIndexOfSource)
          val _tmpUpdatedAt: LocalDateTime
          val _tmp_2: String?
          if (_stmt.isNull(_columnIndexOfUpdatedAt)) {
            _tmp_2 = null
          } else {
            _tmp_2 = _stmt.getText(_columnIndexOfUpdatedAt)
          }
          val _tmp_3: LocalDateTime? = __converters.toLocalDateTime(_tmp_2)
          if (_tmp_3 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_3
          }
          _result = ExchangeRateEntity(_tmpCurrencyPair,_tmpRate,_tmpSource,_tmpUpdatedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getAll(): Flow<List<ExchangeRateEntity>> {
    val _sql: String = "SELECT * FROM exchange_rates ORDER BY currencyPair"
    return createFlow(__db, false, arrayOf("exchange_rates")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfCurrencyPair: Int = getColumnIndexOrThrow(_stmt, "currencyPair")
        val _columnIndexOfRate: Int = getColumnIndexOrThrow(_stmt, "rate")
        val _columnIndexOfSource: Int = getColumnIndexOrThrow(_stmt, "source")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _result: MutableList<ExchangeRateEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ExchangeRateEntity
          val _tmpCurrencyPair: String
          _tmpCurrencyPair = _stmt.getText(_columnIndexOfCurrencyPair)
          val _tmpRate: BigDecimal
          val _tmp: String?
          if (_stmt.isNull(_columnIndexOfRate)) {
            _tmp = null
          } else {
            _tmp = _stmt.getText(_columnIndexOfRate)
          }
          val _tmp_1: BigDecimal? = __converters.toBigDecimal(_tmp)
          if (_tmp_1 == null) {
            error("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.")
          } else {
            _tmpRate = _tmp_1
          }
          val _tmpSource: String
          _tmpSource = _stmt.getText(_columnIndexOfSource)
          val _tmpUpdatedAt: LocalDateTime
          val _tmp_2: String?
          if (_stmt.isNull(_columnIndexOfUpdatedAt)) {
            _tmp_2 = null
          } else {
            _tmp_2 = _stmt.getText(_columnIndexOfUpdatedAt)
          }
          val _tmp_3: LocalDateTime? = __converters.toLocalDateTime(_tmp_2)
          if (_tmp_3 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_3
          }
          _item = ExchangeRateEntity(_tmpCurrencyPair,_tmpRate,_tmpSource,_tmpUpdatedAt)
          _result.add(_item)
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
