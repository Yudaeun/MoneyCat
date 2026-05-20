package com.moneycat.`data`.local.db.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.moneycat.`data`.local.db.converter.Converters
import com.moneycat.`data`.local.db.entity.BudgetEntity
import java.math.BigDecimal
import javax.`annotation`.processing.Generated
import kotlin.Float
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
public class BudgetDao_Impl(
  __db: RoomDatabase,
) : BudgetDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfBudgetEntity: EntityInsertAdapter<BudgetEntity>

  private val __converters: Converters = Converters()

  private val __deleteAdapterOfBudgetEntity: EntityDeleteOrUpdateAdapter<BudgetEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfBudgetEntity = object : EntityInsertAdapter<BudgetEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `budgets` (`id`,`category`,`yearMonth`,`limitAmount`,`alertThreshold`) VALUES (nullif(?, 0),?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: BudgetEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.category)
        statement.bindText(3, entity.yearMonth)
        val _tmp: String? = __converters.fromBigDecimal(entity.limitAmount)
        if (_tmp == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmp)
        }
        statement.bindDouble(5, entity.alertThreshold.toDouble())
      }
    }
    this.__deleteAdapterOfBudgetEntity = object : EntityDeleteOrUpdateAdapter<BudgetEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `budgets` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: BudgetEntity) {
        statement.bindLong(1, entity.id)
      }
    }
  }

  public override suspend fun upsert(budget: BudgetEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __insertAdapterOfBudgetEntity.insert(_connection, budget)
  }

  public override suspend fun delete(budget: BudgetEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __deleteAdapterOfBudgetEntity.handle(_connection, budget)
  }

  public override fun getByMonth(yearMonth: String): Flow<List<BudgetEntity>> {
    val _sql: String = "SELECT * FROM budgets WHERE yearMonth = ?"
    return createFlow(__db, false, arrayOf("budgets")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, yearMonth)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfYearMonth: Int = getColumnIndexOrThrow(_stmt, "yearMonth")
        val _columnIndexOfLimitAmount: Int = getColumnIndexOrThrow(_stmt, "limitAmount")
        val _columnIndexOfAlertThreshold: Int = getColumnIndexOrThrow(_stmt, "alertThreshold")
        val _result: MutableList<BudgetEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: BudgetEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpCategory: String
          _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          val _tmpYearMonth: String
          _tmpYearMonth = _stmt.getText(_columnIndexOfYearMonth)
          val _tmpLimitAmount: BigDecimal
          val _tmp: String?
          if (_stmt.isNull(_columnIndexOfLimitAmount)) {
            _tmp = null
          } else {
            _tmp = _stmt.getText(_columnIndexOfLimitAmount)
          }
          val _tmp_1: BigDecimal? = __converters.toBigDecimal(_tmp)
          if (_tmp_1 == null) {
            error("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.")
          } else {
            _tmpLimitAmount = _tmp_1
          }
          val _tmpAlertThreshold: Float
          _tmpAlertThreshold = _stmt.getDouble(_columnIndexOfAlertThreshold).toFloat()
          _item = BudgetEntity(_tmpId,_tmpCategory,_tmpYearMonth,_tmpLimitAmount,_tmpAlertThreshold)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getByCategoryAndMonth(category: String, yearMonth: String):
      Flow<BudgetEntity?> {
    val _sql: String = "SELECT * FROM budgets WHERE category = ? AND yearMonth = ? LIMIT 1"
    return createFlow(__db, false, arrayOf("budgets")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, category)
        _argIndex = 2
        _stmt.bindText(_argIndex, yearMonth)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfYearMonth: Int = getColumnIndexOrThrow(_stmt, "yearMonth")
        val _columnIndexOfLimitAmount: Int = getColumnIndexOrThrow(_stmt, "limitAmount")
        val _columnIndexOfAlertThreshold: Int = getColumnIndexOrThrow(_stmt, "alertThreshold")
        val _result: BudgetEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpCategory: String
          _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          val _tmpYearMonth: String
          _tmpYearMonth = _stmt.getText(_columnIndexOfYearMonth)
          val _tmpLimitAmount: BigDecimal
          val _tmp: String?
          if (_stmt.isNull(_columnIndexOfLimitAmount)) {
            _tmp = null
          } else {
            _tmp = _stmt.getText(_columnIndexOfLimitAmount)
          }
          val _tmp_1: BigDecimal? = __converters.toBigDecimal(_tmp)
          if (_tmp_1 == null) {
            error("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.")
          } else {
            _tmpLimitAmount = _tmp_1
          }
          val _tmpAlertThreshold: Float
          _tmpAlertThreshold = _stmt.getDouble(_columnIndexOfAlertThreshold).toFloat()
          _result =
              BudgetEntity(_tmpId,_tmpCategory,_tmpYearMonth,_tmpLimitAmount,_tmpAlertThreshold)
        } else {
          _result = null
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
