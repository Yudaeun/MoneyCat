package com.moneycat.domain.repository

import com.moneycat.domain.model.Asset
import kotlinx.coroutines.flow.Flow

interface AssetRepository {
    fun getAll(): Flow<List<Asset>>
    suspend fun insert(asset: Asset): Long
    suspend fun update(asset: Asset)
    suspend fun delete(asset: Asset)
}
