package com.moneycat.`data`.local.db.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.moneycat.`data`.local.db.converter.Converters
import com.moneycat.`data`.local.db.entity.AssetEntity
import com.moneycat.`data`.local.db.model.AssetTypeTotal
import com.moneycat.domain.model.AssetType
import com.moneycat.domain.model.Currency
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.`annotation`.processing.Generated
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
public class AssetDao_Impl(
  __db: RoomDatabase,
) : AssetDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfAssetEntity: EntityInsertAdapter<AssetEntity>

  private val __converters: Converters = Converters()

  private val __deleteAdapterOfAssetEntity: EntityDeleteOrUpdateAdapter<AssetEntity>

  private val __updateAdapterOfAssetEntity: EntityDeleteOrUpdateAdapter<AssetEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfAssetEntity = object : EntityInsertAdapter<AssetEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `assets` (`id`,`name`,`type`,`currency`,`balance`,`description`,`updatedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: AssetEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.name)
        val _tmp: String? = __converters.fromAssetType(entity.type)
        if (_tmp == null) {
          statement.bindNull(3)
        } else {
          statement.bindText(3, _tmp)
        }
        val _tmp_1: String? = __converters.fromCurrency(entity.currency)
        if (_tmp_1 == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmp_1)
        }
        val _tmp_2: String? = __converters.fromBigDecimal(entity.balance)
        if (_tmp_2 == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmp_2)
        }
        val _tmpDescription: String? = entity.description
        if (_tmpDescription == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpDescription)
        }
        val _tmp_3: String? = __converters.fromLocalDateTime(entity.updatedAt)
        if (_tmp_3 == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmp_3)
        }
      }
    }
    this.__deleteAdapterOfAssetEntity = object : EntityDeleteOrUpdateAdapter<AssetEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `assets` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: AssetEntity) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfAssetEntity = object : EntityDeleteOrUpdateAdapter<AssetEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `assets` SET `id` = ?,`name` = ?,`type` = ?,`currency` = ?,`balance` = ?,`description` = ?,`updatedAt` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: AssetEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.name)
        val _tmp: String? = __converters.fromAssetType(entity.type)
        if (_tmp == null) {
          statement.bindNull(3)
        } else {
          statement.bindText(3, _tmp)
        }
        val _tmp_1: String? = __converters.fromCurrency(entity.currency)
        if (_tmp_1 == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmp_1)
        }
        val _tmp_2: String? = __converters.fromBigDecimal(entity.balance)
        if (_tmp_2 == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmp_2)
        }
        val _tmpDescription: String? = entity.description
        if (_tmpDescription == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpDescription)
        }
        val _tmp_3: String? = __converters.fromLocalDateTime(entity.updatedAt)
        if (_tmp_3 == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmp_3)
        }
        statement.bindLong(8, entity.id)
      }
    }
  }

  public override suspend fun insert(asset: AssetEntity): Long = performSuspending(__db, false,
      true) { _connection ->
    val _result: Long = __insertAdapterOfAssetEntity.insertAndReturnId(_connection, asset)
    _result
  }

  public override suspend fun delete(asset: AssetEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __deleteAdapterOfAssetEntity.handle(_connection, asset)
  }

  public override suspend fun update(asset: AssetEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __updateAdapterOfAssetEntity.handle(_connection, asset)
  }

  public override fun getAll(): Flow<List<AssetEntity>> {
    val _sql: String = "SELECT * FROM assets ORDER BY type, name"
    return createFlow(__db, false, arrayOf("assets")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfCurrency: Int = getColumnIndexOrThrow(_stmt, "currency")
        val _columnIndexOfBalance: Int = getColumnIndexOrThrow(_stmt, "balance")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _result: MutableList<AssetEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: AssetEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpType: AssetType
          val _tmp: String?
          if (_stmt.isNull(_columnIndexOfType)) {
            _tmp = null
          } else {
            _tmp = _stmt.getText(_columnIndexOfType)
          }
          val _tmp_1: AssetType? = __converters.toAssetType(_tmp)
          if (_tmp_1 == null) {
            error("Expected NON-NULL 'com.moneycat.domain.model.AssetType', but it was NULL.")
          } else {
            _tmpType = _tmp_1
          }
          val _tmpCurrency: Currency
          val _tmp_2: String?
          if (_stmt.isNull(_columnIndexOfCurrency)) {
            _tmp_2 = null
          } else {
            _tmp_2 = _stmt.getText(_columnIndexOfCurrency)
          }
          val _tmp_3: Currency? = __converters.toCurrency(_tmp_2)
          if (_tmp_3 == null) {
            error("Expected NON-NULL 'com.moneycat.domain.model.Currency', but it was NULL.")
          } else {
            _tmpCurrency = _tmp_3
          }
          val _tmpBalance: BigDecimal
          val _tmp_4: String?
          if (_stmt.isNull(_columnIndexOfBalance)) {
            _tmp_4 = null
          } else {
            _tmp_4 = _stmt.getText(_columnIndexOfBalance)
          }
          val _tmp_5: BigDecimal? = __converters.toBigDecimal(_tmp_4)
          if (_tmp_5 == null) {
            error("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.")
          } else {
            _tmpBalance = _tmp_5
          }
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpUpdatedAt: LocalDateTime
          val _tmp_6: String?
          if (_stmt.isNull(_columnIndexOfUpdatedAt)) {
            _tmp_6 = null
          } else {
            _tmp_6 = _stmt.getText(_columnIndexOfUpdatedAt)
          }
          val _tmp_7: LocalDateTime? = __converters.toLocalDateTime(_tmp_6)
          if (_tmp_7 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_7
          }
          _item =
              AssetEntity(_tmpId,_tmpName,_tmpType,_tmpCurrency,_tmpBalance,_tmpDescription,_tmpUpdatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getTotalByType(): Flow<List<AssetTypeTotal>> {
    val _sql: String = "SELECT type, SUM(balance) as total FROM assets GROUP BY type"
    return createFlow(__db, false, arrayOf("assets")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfType: Int = 0
        val _columnIndexOfTotal: Int = 1
        val _result: MutableList<AssetTypeTotal> = mutableListOf()
        while (_stmt.step()) {
          val _item: AssetTypeTotal
          val _tmpType: AssetType
          val _tmp: String?
          if (_stmt.isNull(_columnIndexOfType)) {
            _tmp = null
          } else {
            _tmp = _stmt.getText(_columnIndexOfType)
          }
          val _tmp_1: AssetType? = __converters.toAssetType(_tmp)
          if (_tmp_1 == null) {
            error("Expected NON-NULL 'com.moneycat.domain.model.AssetType', but it was NULL.")
          } else {
            _tmpType = _tmp_1
          }
          val _tmpTotal: String
          _tmpTotal = _stmt.getText(_columnIndexOfTotal)
          _item = AssetTypeTotal(_tmpType,_tmpTotal)
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
