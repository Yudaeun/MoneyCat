package com.moneycat.`data`.local.db.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.moneycat.`data`.local.db.converter.Converters
import com.moneycat.`data`.local.db.entity.CardBenefitEntity
import java.math.BigDecimal
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class CardBenefitDao_Impl(
  __db: RoomDatabase,
) : CardBenefitDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfCardBenefitEntity: EntityInsertAdapter<CardBenefitEntity>

  private val __converters: Converters = Converters()
  init {
    this.__db = __db
    this.__insertAdapterOfCardBenefitEntity = object : EntityInsertAdapter<CardBenefitEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `card_benefits` (`id`,`cardId`,`category`,`benefitType`,`discountRate`,`monthlyLimit`,`description`) VALUES (nullif(?, 0),?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: CardBenefitEntity) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.cardId)
        statement.bindText(3, entity.category)
        val _tmp: String? = __converters.fromBenefitType(entity.benefitType)
        if (_tmp == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmp)
        }
        val _tmpDiscountRate: BigDecimal? = entity.discountRate
        val _tmp_1: String? = __converters.fromBigDecimal(_tmpDiscountRate)
        if (_tmp_1 == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmp_1)
        }
        val _tmpMonthlyLimit: BigDecimal? = entity.monthlyLimit
        val _tmp_2: String? = __converters.fromBigDecimal(_tmpMonthlyLimit)
        if (_tmp_2 == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmp_2)
        }
        statement.bindText(7, entity.description)
      }
    }
  }

  public override suspend fun insertAll(benefits: List<CardBenefitEntity>): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfCardBenefitEntity.insert(_connection, benefits)
  }

  public override suspend fun deleteByCardId(cardId: Long) {
    val _sql: String = "DELETE FROM card_benefits WHERE cardId = ?"
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

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
