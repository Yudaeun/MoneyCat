package com.moneycat.`data`.local.db.dao

import androidx.collection.LongSparseArray
import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.appendPlaceholders
import androidx.room.util.getColumnIndex
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.room.util.recursiveFetchLongSparseArray
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.SQLiteStatement
import com.moneycat.`data`.local.db.converter.Converters
import com.moneycat.`data`.local.db.entity.CardBenefitEntity
import com.moneycat.`data`.local.db.entity.CardEntity
import com.moneycat.`data`.local.db.relation.CardWithBenefits
import com.moneycat.domain.model.BenefitType
import com.moneycat.domain.model.CardType
import java.math.BigDecimal
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
import kotlin.text.StringBuilder
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class CardDao_Impl(
  __db: RoomDatabase,
) : CardDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfCardEntity: EntityInsertAdapter<CardEntity>

  private val __converters: Converters = Converters()

  private val __updateAdapterOfCardEntity: EntityDeleteOrUpdateAdapter<CardEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfCardEntity = object : EntityInsertAdapter<CardEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `cards` (`id`,`name`,`bankName`,`type`,`lastFourDigits`,`isActive`,`annualFee`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: CardEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.bankName)
        val _tmp: String? = __converters.fromCardType(entity.type)
        if (_tmp == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmp)
        }
        val _tmpLastFourDigits: String? = entity.lastFourDigits
        if (_tmpLastFourDigits == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpLastFourDigits)
        }
        val _tmp_1: Int = if (entity.isActive) 1 else 0
        statement.bindLong(6, _tmp_1.toLong())
        val _tmpAnnualFee: BigDecimal? = entity.annualFee
        val _tmp_2: String? = __converters.fromBigDecimal(_tmpAnnualFee)
        if (_tmp_2 == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmp_2)
        }
        val _tmp_3: String? = __converters.fromLocalDateTime(entity.createdAt)
        if (_tmp_3 == null) {
          statement.bindNull(8)
        } else {
          statement.bindText(8, _tmp_3)
        }
      }
    }
    this.__updateAdapterOfCardEntity = object : EntityDeleteOrUpdateAdapter<CardEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `cards` SET `id` = ?,`name` = ?,`bankName` = ?,`type` = ?,`lastFourDigits` = ?,`isActive` = ?,`annualFee` = ?,`createdAt` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: CardEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.bankName)
        val _tmp: String? = __converters.fromCardType(entity.type)
        if (_tmp == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmp)
        }
        val _tmpLastFourDigits: String? = entity.lastFourDigits
        if (_tmpLastFourDigits == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpLastFourDigits)
        }
        val _tmp_1: Int = if (entity.isActive) 1 else 0
        statement.bindLong(6, _tmp_1.toLong())
        val _tmpAnnualFee: BigDecimal? = entity.annualFee
        val _tmp_2: String? = __converters.fromBigDecimal(_tmpAnnualFee)
        if (_tmp_2 == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmp_2)
        }
        val _tmp_3: String? = __converters.fromLocalDateTime(entity.createdAt)
        if (_tmp_3 == null) {
          statement.bindNull(8)
        } else {
          statement.bindText(8, _tmp_3)
        }
        statement.bindLong(9, entity.id)
      }
    }
  }

  public override suspend fun insert(card: CardEntity): Long = performSuspending(__db, false, true)
      { _connection ->
    val _result: Long = __insertAdapterOfCardEntity.insertAndReturnId(_connection, card)
    _result
  }

  public override suspend fun update(card: CardEntity): Unit = performSuspending(__db, false, true)
      { _connection ->
    __updateAdapterOfCardEntity.handle(_connection, card)
  }

  public override fun getActiveCards(): Flow<List<CardEntity>> {
    val _sql: String = "SELECT * FROM cards WHERE isActive = 1 ORDER BY bankName, name"
    return createFlow(__db, false, arrayOf("cards")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfBankName: Int = getColumnIndexOrThrow(_stmt, "bankName")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfLastFourDigits: Int = getColumnIndexOrThrow(_stmt, "lastFourDigits")
        val _columnIndexOfIsActive: Int = getColumnIndexOrThrow(_stmt, "isActive")
        val _columnIndexOfAnnualFee: Int = getColumnIndexOrThrow(_stmt, "annualFee")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: MutableList<CardEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: CardEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpBankName: String
          _tmpBankName = _stmt.getText(_columnIndexOfBankName)
          val _tmpType: CardType
          val _tmp: String?
          if (_stmt.isNull(_columnIndexOfType)) {
            _tmp = null
          } else {
            _tmp = _stmt.getText(_columnIndexOfType)
          }
          val _tmp_1: CardType? = __converters.toCardType(_tmp)
          if (_tmp_1 == null) {
            error("Expected NON-NULL 'com.moneycat.domain.model.CardType', but it was NULL.")
          } else {
            _tmpType = _tmp_1
          }
          val _tmpLastFourDigits: String?
          if (_stmt.isNull(_columnIndexOfLastFourDigits)) {
            _tmpLastFourDigits = null
          } else {
            _tmpLastFourDigits = _stmt.getText(_columnIndexOfLastFourDigits)
          }
          val _tmpIsActive: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfIsActive).toInt()
          _tmpIsActive = _tmp_2 != 0
          val _tmpAnnualFee: BigDecimal?
          val _tmp_3: String?
          if (_stmt.isNull(_columnIndexOfAnnualFee)) {
            _tmp_3 = null
          } else {
            _tmp_3 = _stmt.getText(_columnIndexOfAnnualFee)
          }
          _tmpAnnualFee = __converters.toBigDecimal(_tmp_3)
          val _tmpCreatedAt: LocalDateTime
          val _tmp_4: String?
          if (_stmt.isNull(_columnIndexOfCreatedAt)) {
            _tmp_4 = null
          } else {
            _tmp_4 = _stmt.getText(_columnIndexOfCreatedAt)
          }
          val _tmp_5: LocalDateTime? = __converters.toLocalDateTime(_tmp_4)
          if (_tmp_5 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpCreatedAt = _tmp_5
          }
          _item =
              CardEntity(_tmpId,_tmpName,_tmpBankName,_tmpType,_tmpLastFourDigits,_tmpIsActive,_tmpAnnualFee,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getCardWithBenefits(cardId: Long): Flow<CardWithBenefits?> {
    val _sql: String = "SELECT * FROM cards WHERE id = ?"
    return createFlow(__db, true, arrayOf("card_benefits", "cards")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, cardId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfBankName: Int = getColumnIndexOrThrow(_stmt, "bankName")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfLastFourDigits: Int = getColumnIndexOrThrow(_stmt, "lastFourDigits")
        val _columnIndexOfIsActive: Int = getColumnIndexOrThrow(_stmt, "isActive")
        val _columnIndexOfAnnualFee: Int = getColumnIndexOrThrow(_stmt, "annualFee")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _collectionBenefits: LongSparseArray<MutableList<CardBenefitEntity>> =
            LongSparseArray<MutableList<CardBenefitEntity>>()
        while (_stmt.step()) {
          val _tmpKey: Long
          _tmpKey = _stmt.getLong(_columnIndexOfId)
          if (!_collectionBenefits.containsKey(_tmpKey)) {
            _collectionBenefits.put(_tmpKey, mutableListOf())
          }
        }
        _stmt.reset()
        __fetchRelationshipcardBenefitsAscomMoneycatDataLocalDbEntityCardBenefitEntity(_connection,
            _collectionBenefits)
        val _result: CardWithBenefits?
        if (_stmt.step()) {
          val _tmpCard: CardEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpBankName: String
          _tmpBankName = _stmt.getText(_columnIndexOfBankName)
          val _tmpType: CardType
          val _tmp: String?
          if (_stmt.isNull(_columnIndexOfType)) {
            _tmp = null
          } else {
            _tmp = _stmt.getText(_columnIndexOfType)
          }
          val _tmp_1: CardType? = __converters.toCardType(_tmp)
          if (_tmp_1 == null) {
            error("Expected NON-NULL 'com.moneycat.domain.model.CardType', but it was NULL.")
          } else {
            _tmpType = _tmp_1
          }
          val _tmpLastFourDigits: String?
          if (_stmt.isNull(_columnIndexOfLastFourDigits)) {
            _tmpLastFourDigits = null
          } else {
            _tmpLastFourDigits = _stmt.getText(_columnIndexOfLastFourDigits)
          }
          val _tmpIsActive: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfIsActive).toInt()
          _tmpIsActive = _tmp_2 != 0
          val _tmpAnnualFee: BigDecimal?
          val _tmp_3: String?
          if (_stmt.isNull(_columnIndexOfAnnualFee)) {
            _tmp_3 = null
          } else {
            _tmp_3 = _stmt.getText(_columnIndexOfAnnualFee)
          }
          _tmpAnnualFee = __converters.toBigDecimal(_tmp_3)
          val _tmpCreatedAt: LocalDateTime
          val _tmp_4: String?
          if (_stmt.isNull(_columnIndexOfCreatedAt)) {
            _tmp_4 = null
          } else {
            _tmp_4 = _stmt.getText(_columnIndexOfCreatedAt)
          }
          val _tmp_5: LocalDateTime? = __converters.toLocalDateTime(_tmp_4)
          if (_tmp_5 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpCreatedAt = _tmp_5
          }
          _tmpCard =
              CardEntity(_tmpId,_tmpName,_tmpBankName,_tmpType,_tmpLastFourDigits,_tmpIsActive,_tmpAnnualFee,_tmpCreatedAt)
          val _tmpBenefitsCollection: MutableList<CardBenefitEntity>
          val _tmpKey_1: Long
          _tmpKey_1 = _stmt.getLong(_columnIndexOfId)
          _tmpBenefitsCollection = checkNotNull(_collectionBenefits.get(_tmpKey_1))
          _result = CardWithBenefits(_tmpCard,_tmpBenefitsCollection)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun softDelete(cardId: Long) {
    val _sql: String = "UPDATE cards SET isActive = 0 WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, cardId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  private
      fun __fetchRelationshipcardBenefitsAscomMoneycatDataLocalDbEntityCardBenefitEntity(_connection: SQLiteConnection,
      _map: LongSparseArray<MutableList<CardBenefitEntity>>) {
    if (_map.isEmpty()) {
      return
    }
    if (_map.size() > 999) {
      recursiveFetchLongSparseArray(_map, true) { _tmpMap ->
        __fetchRelationshipcardBenefitsAscomMoneycatDataLocalDbEntityCardBenefitEntity(_connection,
            _tmpMap)
      }
      return
    }
    val _stringBuilder: StringBuilder = StringBuilder()
    _stringBuilder.append("SELECT `id`,`cardId`,`category`,`benefitType`,`discountRate`,`monthlyLimit`,`description` FROM `card_benefits` WHERE `cardId` IN (")
    val _inputSize: Int = _map.size()
    appendPlaceholders(_stringBuilder, _inputSize)
    _stringBuilder.append(")")
    val _sql: String = _stringBuilder.toString()
    val _stmt: SQLiteStatement = _connection.prepare(_sql)
    var _argIndex: Int = 1
    for (i in 0 until _map.size()) {
      val _item: Long = _map.keyAt(i)
      _stmt.bindLong(_argIndex, _item)
      _argIndex++
    }
    try {
      val _itemKeyIndex: Int = getColumnIndex(_stmt, "cardId")
      if (_itemKeyIndex == -1) {
        return
      }
      val _columnIndexOfId: Int = 0
      val _columnIndexOfCardId: Int = 1
      val _columnIndexOfCategory: Int = 2
      val _columnIndexOfBenefitType: Int = 3
      val _columnIndexOfDiscountRate: Int = 4
      val _columnIndexOfMonthlyLimit: Int = 5
      val _columnIndexOfDescription: Int = 6
      while (_stmt.step()) {
        val _tmpKey: Long
        _tmpKey = _stmt.getLong(_itemKeyIndex)
        val _tmpRelation: MutableList<CardBenefitEntity>? = _map.get(_tmpKey)
        if (_tmpRelation != null) {
          val _item_1: CardBenefitEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpCardId: Long
          _tmpCardId = _stmt.getLong(_columnIndexOfCardId)
          val _tmpCategory: String
          _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          val _tmpBenefitType: BenefitType
          val _tmp: String?
          if (_stmt.isNull(_columnIndexOfBenefitType)) {
            _tmp = null
          } else {
            _tmp = _stmt.getText(_columnIndexOfBenefitType)
          }
          val _tmp_1: BenefitType? = __converters.toBenefitType(_tmp)
          if (_tmp_1 == null) {
            error("Expected NON-NULL 'com.moneycat.domain.model.BenefitType', but it was NULL.")
          } else {
            _tmpBenefitType = _tmp_1
          }
          val _tmpDiscountRate: BigDecimal?
          val _tmp_2: String?
          if (_stmt.isNull(_columnIndexOfDiscountRate)) {
            _tmp_2 = null
          } else {
            _tmp_2 = _stmt.getText(_columnIndexOfDiscountRate)
          }
          _tmpDiscountRate = __converters.toBigDecimal(_tmp_2)
          val _tmpMonthlyLimit: BigDecimal?
          val _tmp_3: String?
          if (_stmt.isNull(_columnIndexOfMonthlyLimit)) {
            _tmp_3 = null
          } else {
            _tmp_3 = _stmt.getText(_columnIndexOfMonthlyLimit)
          }
          _tmpMonthlyLimit = __converters.toBigDecimal(_tmp_3)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          _item_1 =
              CardBenefitEntity(_tmpId,_tmpCardId,_tmpCategory,_tmpBenefitType,_tmpDiscountRate,_tmpMonthlyLimit,_tmpDescription)
          _tmpRelation.add(_item_1)
        }
      }
    } finally {
      _stmt.close()
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
