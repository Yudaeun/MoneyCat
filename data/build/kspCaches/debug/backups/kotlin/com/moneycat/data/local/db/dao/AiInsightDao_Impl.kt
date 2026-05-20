package com.moneycat.`data`.local.db.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.moneycat.`data`.local.db.converter.Converters
import com.moneycat.`data`.local.db.entity.AiInsightEntity
import com.moneycat.domain.model.InsightType
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
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class AiInsightDao_Impl(
  __db: RoomDatabase,
) : AiInsightDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfAiInsightEntity: EntityInsertAdapter<AiInsightEntity>

  private val __converters: Converters = Converters()
  init {
    this.__db = __db
    this.__insertAdapterOfAiInsightEntity = object : EntityInsertAdapter<AiInsightEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `ai_insights` (`id`,`type`,`title`,`content`,`estimatedSaving`,`isRead`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: AiInsightEntity) {
        statement.bindLong(1, entity.id)
        val _tmp: String? = __converters.fromInsightType(entity.type)
        if (_tmp == null) {
          statement.bindNull(2)
        } else {
          statement.bindText(2, _tmp)
        }
        statement.bindText(3, entity.title)
        statement.bindText(4, entity.content)
        val _tmpEstimatedSaving: BigDecimal? = entity.estimatedSaving
        val _tmp_1: String? = __converters.fromBigDecimal(_tmpEstimatedSaving)
        if (_tmp_1 == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmp_1)
        }
        val _tmp_2: Int = if (entity.isRead) 1 else 0
        statement.bindLong(6, _tmp_2.toLong())
        val _tmp_3: String? = __converters.fromLocalDateTime(entity.createdAt)
        if (_tmp_3 == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmp_3)
        }
      }
    }
  }

  public override suspend fun insert(insight: AiInsightEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __insertAdapterOfAiInsightEntity.insert(_connection, insight)
  }

  public override fun getAll(): Flow<List<AiInsightEntity>> {
    val _sql: String = "SELECT * FROM ai_insights ORDER BY createdAt DESC"
    return createFlow(__db, false, arrayOf("ai_insights")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfContent: Int = getColumnIndexOrThrow(_stmt, "content")
        val _columnIndexOfEstimatedSaving: Int = getColumnIndexOrThrow(_stmt, "estimatedSaving")
        val _columnIndexOfIsRead: Int = getColumnIndexOrThrow(_stmt, "isRead")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: MutableList<AiInsightEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: AiInsightEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpType: InsightType
          val _tmp: String?
          if (_stmt.isNull(_columnIndexOfType)) {
            _tmp = null
          } else {
            _tmp = _stmt.getText(_columnIndexOfType)
          }
          val _tmp_1: InsightType? = __converters.toInsightType(_tmp)
          if (_tmp_1 == null) {
            error("Expected NON-NULL 'com.moneycat.domain.model.InsightType', but it was NULL.")
          } else {
            _tmpType = _tmp_1
          }
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpContent: String
          _tmpContent = _stmt.getText(_columnIndexOfContent)
          val _tmpEstimatedSaving: BigDecimal?
          val _tmp_2: String?
          if (_stmt.isNull(_columnIndexOfEstimatedSaving)) {
            _tmp_2 = null
          } else {
            _tmp_2 = _stmt.getText(_columnIndexOfEstimatedSaving)
          }
          _tmpEstimatedSaving = __converters.toBigDecimal(_tmp_2)
          val _tmpIsRead: Boolean
          val _tmp_3: Int
          _tmp_3 = _stmt.getLong(_columnIndexOfIsRead).toInt()
          _tmpIsRead = _tmp_3 != 0
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
              AiInsightEntity(_tmpId,_tmpType,_tmpTitle,_tmpContent,_tmpEstimatedSaving,_tmpIsRead,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getUnread(): Flow<List<AiInsightEntity>> {
    val _sql: String = "SELECT * FROM ai_insights WHERE isRead = 0 ORDER BY createdAt DESC"
    return createFlow(__db, false, arrayOf("ai_insights")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfContent: Int = getColumnIndexOrThrow(_stmt, "content")
        val _columnIndexOfEstimatedSaving: Int = getColumnIndexOrThrow(_stmt, "estimatedSaving")
        val _columnIndexOfIsRead: Int = getColumnIndexOrThrow(_stmt, "isRead")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: MutableList<AiInsightEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: AiInsightEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpType: InsightType
          val _tmp: String?
          if (_stmt.isNull(_columnIndexOfType)) {
            _tmp = null
          } else {
            _tmp = _stmt.getText(_columnIndexOfType)
          }
          val _tmp_1: InsightType? = __converters.toInsightType(_tmp)
          if (_tmp_1 == null) {
            error("Expected NON-NULL 'com.moneycat.domain.model.InsightType', but it was NULL.")
          } else {
            _tmpType = _tmp_1
          }
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpContent: String
          _tmpContent = _stmt.getText(_columnIndexOfContent)
          val _tmpEstimatedSaving: BigDecimal?
          val _tmp_2: String?
          if (_stmt.isNull(_columnIndexOfEstimatedSaving)) {
            _tmp_2 = null
          } else {
            _tmp_2 = _stmt.getText(_columnIndexOfEstimatedSaving)
          }
          _tmpEstimatedSaving = __converters.toBigDecimal(_tmp_2)
          val _tmpIsRead: Boolean
          val _tmp_3: Int
          _tmp_3 = _stmt.getLong(_columnIndexOfIsRead).toInt()
          _tmpIsRead = _tmp_3 != 0
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
              AiInsightEntity(_tmpId,_tmpType,_tmpTitle,_tmpContent,_tmpEstimatedSaving,_tmpIsRead,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getLatest(): Flow<AiInsightEntity?> {
    val _sql: String = "SELECT * FROM ai_insights ORDER BY createdAt DESC LIMIT 1"
    return createFlow(__db, false, arrayOf("ai_insights")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfContent: Int = getColumnIndexOrThrow(_stmt, "content")
        val _columnIndexOfEstimatedSaving: Int = getColumnIndexOrThrow(_stmt, "estimatedSaving")
        val _columnIndexOfIsRead: Int = getColumnIndexOrThrow(_stmt, "isRead")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: AiInsightEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpType: InsightType
          val _tmp: String?
          if (_stmt.isNull(_columnIndexOfType)) {
            _tmp = null
          } else {
            _tmp = _stmt.getText(_columnIndexOfType)
          }
          val _tmp_1: InsightType? = __converters.toInsightType(_tmp)
          if (_tmp_1 == null) {
            error("Expected NON-NULL 'com.moneycat.domain.model.InsightType', but it was NULL.")
          } else {
            _tmpType = _tmp_1
          }
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpContent: String
          _tmpContent = _stmt.getText(_columnIndexOfContent)
          val _tmpEstimatedSaving: BigDecimal?
          val _tmp_2: String?
          if (_stmt.isNull(_columnIndexOfEstimatedSaving)) {
            _tmp_2 = null
          } else {
            _tmp_2 = _stmt.getText(_columnIndexOfEstimatedSaving)
          }
          _tmpEstimatedSaving = __converters.toBigDecimal(_tmp_2)
          val _tmpIsRead: Boolean
          val _tmp_3: Int
          _tmp_3 = _stmt.getLong(_columnIndexOfIsRead).toInt()
          _tmpIsRead = _tmp_3 != 0
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
          _result =
              AiInsightEntity(_tmpId,_tmpType,_tmpTitle,_tmpContent,_tmpEstimatedSaving,_tmpIsRead,_tmpCreatedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun markAsRead(id: Long) {
    val _sql: String = "UPDATE ai_insights SET isRead = 1 WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
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
