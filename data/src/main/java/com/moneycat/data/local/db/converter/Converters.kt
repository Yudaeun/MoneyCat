package com.moneycat.data.local.db.converter

import androidx.room.TypeConverter
import com.moneycat.domain.model.AssetType
import com.moneycat.domain.model.BenefitType
import com.moneycat.domain.model.CardType
import com.moneycat.domain.model.Currency
import com.moneycat.domain.model.InputSource
import com.moneycat.domain.model.InsightType
import com.moneycat.domain.model.PaymentMethod
import com.moneycat.domain.model.TransactionType
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

class Converters {

    // BigDecimal ↔ String
    @TypeConverter fun fromBigDecimal(value: BigDecimal?): String? = value?.toPlainString()
    @TypeConverter fun toBigDecimal(value: String?): BigDecimal? = value?.let { BigDecimal(it) }

    // LocalDate ↔ String (ISO format: YYYY-MM-DD)
    @TypeConverter fun fromLocalDate(value: LocalDate?): String? = value?.toString()
    @TypeConverter fun toLocalDate(value: String?): LocalDate? = value?.let { LocalDate.parse(it) }

    // LocalDateTime ↔ String (ISO format)
    @TypeConverter fun fromLocalDateTime(value: LocalDateTime?): String? = value?.toString()
    @TypeConverter fun toLocalDateTime(value: String?): LocalDateTime? = value?.let { LocalDateTime.parse(it) }

    // Enum ↔ String (name)
    @TypeConverter fun fromAssetType(value: AssetType?): String? = value?.name
    @TypeConverter fun toAssetType(value: String?): AssetType? = value?.let { AssetType.valueOf(it) }

    @TypeConverter fun fromCurrency(value: Currency?): String? = value?.name
    @TypeConverter fun toCurrency(value: String?): Currency? = value?.let { Currency.valueOf(it) }

    @TypeConverter fun fromTransactionType(value: TransactionType?): String? = value?.name
    @TypeConverter fun toTransactionType(value: String?): TransactionType? = value?.let { TransactionType.valueOf(it) }

    @TypeConverter fun fromPaymentMethod(value: PaymentMethod?): String? = value?.name
    @TypeConverter fun toPaymentMethod(value: String?): PaymentMethod? = value?.let { PaymentMethod.valueOf(it) }

    @TypeConverter fun fromCardType(value: CardType?): String? = value?.name
    @TypeConverter fun toCardType(value: String?): CardType? = value?.let { CardType.valueOf(it) }

    @TypeConverter fun fromBenefitType(value: BenefitType?): String? = value?.name
    @TypeConverter fun toBenefitType(value: String?): BenefitType? = value?.let { BenefitType.valueOf(it) }

    @TypeConverter fun fromInputSource(value: InputSource?): String? = value?.name
    @TypeConverter fun toInputSource(value: String?): InputSource? = value?.let { InputSource.valueOf(it) }

    @TypeConverter fun fromInsightType(value: InsightType?): String? = value?.name
    @TypeConverter fun toInsightType(value: String?): InsightType? = value?.let { InsightType.valueOf(it) }
}
