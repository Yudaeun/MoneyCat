package com.moneycat.`data`.local.db.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.moneycat.`data`.local.db.converter.Converters
import com.moneycat.`data`.local.db.entity.UserProfileEntity
import java.math.BigDecimal
import java.time.LocalDate
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class UserProfileDao_Impl(
  __db: RoomDatabase,
) : UserProfileDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfUserProfileEntity: EntityInsertAdapter<UserProfileEntity>

  private val __converters: Converters = Converters()
  init {
    this.__db = __db
    this.__insertAdapterOfUserProfileEntity = object : EntityInsertAdapter<UserProfileEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `user_profiles` (`id`,`name`,`monthlyIncome`,`mainBankName`,`financialGoal`,`onboardingCompleted`,`createdAt`) VALUES (?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: UserProfileEntity) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.name)
        val _tmp: String? = __converters.fromBigDecimal(entity.monthlyIncome)
        if (_tmp == null) {
          statement.bindNull(3)
        } else {
          statement.bindText(3, _tmp)
        }
        val _tmpMainBankName: String? = entity.mainBankName
        if (_tmpMainBankName == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmpMainBankName)
        }
        val _tmpFinancialGoal: String? = entity.financialGoal
        if (_tmpFinancialGoal == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpFinancialGoal)
        }
        val _tmp_1: Int = if (entity.onboardingCompleted) 1 else 0
        statement.bindLong(6, _tmp_1.toLong())
        val _tmp_2: String? = __converters.fromLocalDate(entity.createdAt)
        if (_tmp_2 == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmp_2)
        }
      }
    }
  }

  public override suspend fun upsert(profile: UserProfileEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __insertAdapterOfUserProfileEntity.insert(_connection, profile)
  }

  public override fun `get`(): Flow<UserProfileEntity?> {
    val _sql: String = "SELECT * FROM user_profiles WHERE id = 1 LIMIT 1"
    return createFlow(__db, false, arrayOf("user_profiles")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfMonthlyIncome: Int = getColumnIndexOrThrow(_stmt, "monthlyIncome")
        val _columnIndexOfMainBankName: Int = getColumnIndexOrThrow(_stmt, "mainBankName")
        val _columnIndexOfFinancialGoal: Int = getColumnIndexOrThrow(_stmt, "financialGoal")
        val _columnIndexOfOnboardingCompleted: Int = getColumnIndexOrThrow(_stmt,
            "onboardingCompleted")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: UserProfileEntity?
        if (_stmt.step()) {
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpMonthlyIncome: BigDecimal
          val _tmp: String?
          if (_stmt.isNull(_columnIndexOfMonthlyIncome)) {
            _tmp = null
          } else {
            _tmp = _stmt.getText(_columnIndexOfMonthlyIncome)
          }
          val _tmp_1: BigDecimal? = __converters.toBigDecimal(_tmp)
          if (_tmp_1 == null) {
            error("Expected NON-NULL 'java.math.BigDecimal', but it was NULL.")
          } else {
            _tmpMonthlyIncome = _tmp_1
          }
          val _tmpMainBankName: String?
          if (_stmt.isNull(_columnIndexOfMainBankName)) {
            _tmpMainBankName = null
          } else {
            _tmpMainBankName = _stmt.getText(_columnIndexOfMainBankName)
          }
          val _tmpFinancialGoal: String?
          if (_stmt.isNull(_columnIndexOfFinancialGoal)) {
            _tmpFinancialGoal = null
          } else {
            _tmpFinancialGoal = _stmt.getText(_columnIndexOfFinancialGoal)
          }
          val _tmpOnboardingCompleted: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfOnboardingCompleted).toInt()
          _tmpOnboardingCompleted = _tmp_2 != 0
          val _tmpCreatedAt: LocalDate
          val _tmp_3: String?
          if (_stmt.isNull(_columnIndexOfCreatedAt)) {
            _tmp_3 = null
          } else {
            _tmp_3 = _stmt.getText(_columnIndexOfCreatedAt)
          }
          val _tmp_4: LocalDate? = __converters.toLocalDate(_tmp_3)
          if (_tmp_4 == null) {
            error("Expected NON-NULL 'java.time.LocalDate', but it was NULL.")
          } else {
            _tmpCreatedAt = _tmp_4
          }
          _result =
              UserProfileEntity(_tmpId,_tmpName,_tmpMonthlyIncome,_tmpMainBankName,_tmpFinancialGoal,_tmpOnboardingCompleted,_tmpCreatedAt)
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
