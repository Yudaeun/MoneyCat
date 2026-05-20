package com.moneycat.`data`.local.db.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.moneycat.`data`.local.db.entity.NotificationRuleEntity
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
public class NotificationRuleDao_Impl(
  __db: RoomDatabase,
) : NotificationRuleDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfNotificationRuleEntity: EntityInsertAdapter<NotificationRuleEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfNotificationRuleEntity = object :
        EntityInsertAdapter<NotificationRuleEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `notification_rules` (`id`,`bankName`,`packageName`,`titlePattern`,`bodyPattern`,`amountGroup`,`merchantGroup`,`isEnabled`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: NotificationRuleEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.bankName)
        statement.bindText(3, entity.packageName)
        statement.bindText(4, entity.titlePattern)
        statement.bindText(5, entity.bodyPattern)
        statement.bindLong(6, entity.amountGroup.toLong())
        val _tmpMerchantGroup: Int? = entity.merchantGroup
        if (_tmpMerchantGroup == null) {
          statement.bindNull(7)
        } else {
          statement.bindLong(7, _tmpMerchantGroup.toLong())
        }
        val _tmp: Int = if (entity.isEnabled) 1 else 0
        statement.bindLong(8, _tmp.toLong())
      }
    }
  }

  public override suspend fun insertAll(rules: List<NotificationRuleEntity>): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfNotificationRuleEntity.insert(_connection, rules)
  }

  public override fun getAll(): Flow<List<NotificationRuleEntity>> {
    val _sql: String = "SELECT * FROM notification_rules ORDER BY bankName"
    return createFlow(__db, false, arrayOf("notification_rules")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfBankName: Int = getColumnIndexOrThrow(_stmt, "bankName")
        val _columnIndexOfPackageName: Int = getColumnIndexOrThrow(_stmt, "packageName")
        val _columnIndexOfTitlePattern: Int = getColumnIndexOrThrow(_stmt, "titlePattern")
        val _columnIndexOfBodyPattern: Int = getColumnIndexOrThrow(_stmt, "bodyPattern")
        val _columnIndexOfAmountGroup: Int = getColumnIndexOrThrow(_stmt, "amountGroup")
        val _columnIndexOfMerchantGroup: Int = getColumnIndexOrThrow(_stmt, "merchantGroup")
        val _columnIndexOfIsEnabled: Int = getColumnIndexOrThrow(_stmt, "isEnabled")
        val _result: MutableList<NotificationRuleEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: NotificationRuleEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpBankName: String
          _tmpBankName = _stmt.getText(_columnIndexOfBankName)
          val _tmpPackageName: String
          _tmpPackageName = _stmt.getText(_columnIndexOfPackageName)
          val _tmpTitlePattern: String
          _tmpTitlePattern = _stmt.getText(_columnIndexOfTitlePattern)
          val _tmpBodyPattern: String
          _tmpBodyPattern = _stmt.getText(_columnIndexOfBodyPattern)
          val _tmpAmountGroup: Int
          _tmpAmountGroup = _stmt.getLong(_columnIndexOfAmountGroup).toInt()
          val _tmpMerchantGroup: Int?
          if (_stmt.isNull(_columnIndexOfMerchantGroup)) {
            _tmpMerchantGroup = null
          } else {
            _tmpMerchantGroup = _stmt.getLong(_columnIndexOfMerchantGroup).toInt()
          }
          val _tmpIsEnabled: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsEnabled).toInt()
          _tmpIsEnabled = _tmp != 0
          _item =
              NotificationRuleEntity(_tmpId,_tmpBankName,_tmpPackageName,_tmpTitlePattern,_tmpBodyPattern,_tmpAmountGroup,_tmpMerchantGroup,_tmpIsEnabled)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun findByPackage(packageName: String): NotificationRuleEntity? {
    val _sql: String = "SELECT * FROM notification_rules WHERE packageName = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, packageName)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfBankName: Int = getColumnIndexOrThrow(_stmt, "bankName")
        val _columnIndexOfPackageName: Int = getColumnIndexOrThrow(_stmt, "packageName")
        val _columnIndexOfTitlePattern: Int = getColumnIndexOrThrow(_stmt, "titlePattern")
        val _columnIndexOfBodyPattern: Int = getColumnIndexOrThrow(_stmt, "bodyPattern")
        val _columnIndexOfAmountGroup: Int = getColumnIndexOrThrow(_stmt, "amountGroup")
        val _columnIndexOfMerchantGroup: Int = getColumnIndexOrThrow(_stmt, "merchantGroup")
        val _columnIndexOfIsEnabled: Int = getColumnIndexOrThrow(_stmt, "isEnabled")
        val _result: NotificationRuleEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpBankName: String
          _tmpBankName = _stmt.getText(_columnIndexOfBankName)
          val _tmpPackageName: String
          _tmpPackageName = _stmt.getText(_columnIndexOfPackageName)
          val _tmpTitlePattern: String
          _tmpTitlePattern = _stmt.getText(_columnIndexOfTitlePattern)
          val _tmpBodyPattern: String
          _tmpBodyPattern = _stmt.getText(_columnIndexOfBodyPattern)
          val _tmpAmountGroup: Int
          _tmpAmountGroup = _stmt.getLong(_columnIndexOfAmountGroup).toInt()
          val _tmpMerchantGroup: Int?
          if (_stmt.isNull(_columnIndexOfMerchantGroup)) {
            _tmpMerchantGroup = null
          } else {
            _tmpMerchantGroup = _stmt.getLong(_columnIndexOfMerchantGroup).toInt()
          }
          val _tmpIsEnabled: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsEnabled).toInt()
          _tmpIsEnabled = _tmp != 0
          _result =
              NotificationRuleEntity(_tmpId,_tmpBankName,_tmpPackageName,_tmpTitlePattern,_tmpBodyPattern,_tmpAmountGroup,_tmpMerchantGroup,_tmpIsEnabled)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getEnabled(): List<NotificationRuleEntity> {
    val _sql: String = "SELECT * FROM notification_rules WHERE isEnabled = 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfBankName: Int = getColumnIndexOrThrow(_stmt, "bankName")
        val _columnIndexOfPackageName: Int = getColumnIndexOrThrow(_stmt, "packageName")
        val _columnIndexOfTitlePattern: Int = getColumnIndexOrThrow(_stmt, "titlePattern")
        val _columnIndexOfBodyPattern: Int = getColumnIndexOrThrow(_stmt, "bodyPattern")
        val _columnIndexOfAmountGroup: Int = getColumnIndexOrThrow(_stmt, "amountGroup")
        val _columnIndexOfMerchantGroup: Int = getColumnIndexOrThrow(_stmt, "merchantGroup")
        val _columnIndexOfIsEnabled: Int = getColumnIndexOrThrow(_stmt, "isEnabled")
        val _result: MutableList<NotificationRuleEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: NotificationRuleEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpBankName: String
          _tmpBankName = _stmt.getText(_columnIndexOfBankName)
          val _tmpPackageName: String
          _tmpPackageName = _stmt.getText(_columnIndexOfPackageName)
          val _tmpTitlePattern: String
          _tmpTitlePattern = _stmt.getText(_columnIndexOfTitlePattern)
          val _tmpBodyPattern: String
          _tmpBodyPattern = _stmt.getText(_columnIndexOfBodyPattern)
          val _tmpAmountGroup: Int
          _tmpAmountGroup = _stmt.getLong(_columnIndexOfAmountGroup).toInt()
          val _tmpMerchantGroup: Int?
          if (_stmt.isNull(_columnIndexOfMerchantGroup)) {
            _tmpMerchantGroup = null
          } else {
            _tmpMerchantGroup = _stmt.getLong(_columnIndexOfMerchantGroup).toInt()
          }
          val _tmpIsEnabled: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsEnabled).toInt()
          _tmpIsEnabled = _tmp != 0
          _item =
              NotificationRuleEntity(_tmpId,_tmpBankName,_tmpPackageName,_tmpTitlePattern,_tmpBodyPattern,_tmpAmountGroup,_tmpMerchantGroup,_tmpIsEnabled)
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
