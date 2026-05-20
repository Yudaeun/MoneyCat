package com.moneycat.data.local.db.model

import com.moneycat.domain.model.AssetType

data class CategoryTotal(
    val category: String,
    val total: String   // BigDecimal을 String으로 조회
)

data class CardSpending(
    val cardId: Long?,
    val total: String
)

data class AssetTypeTotal(
    val type: AssetType,
    val total: String
)
