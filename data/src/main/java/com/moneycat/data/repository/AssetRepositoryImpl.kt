package com.moneycat.data.repository

import com.moneycat.data.local.db.dao.AssetDao
import com.moneycat.data.local.db.mapper.toDomain
import com.moneycat.data.local.db.mapper.toEntity
import com.moneycat.domain.model.Asset
import com.moneycat.domain.repository.AssetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssetRepositoryImpl @Inject constructor(
    private val dao: AssetDao
) : AssetRepository {

    override fun getAll(): Flow<List<Asset>> =
        dao.getAll().map { list -> list.map { it.toDomain() } }

    override suspend fun insert(asset: Asset): Long =
        dao.insert(asset.toEntity())

    override suspend fun update(asset: Asset) =
        dao.update(asset.toEntity())

    override suspend fun delete(asset: Asset) =
        dao.delete(asset.toEntity())
}
