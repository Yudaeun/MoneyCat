package com.moneycat.domain.usecase

import com.moneycat.domain.model.Asset
import com.moneycat.domain.repository.AssetRepository
import javax.inject.Inject

class InsertAssetUseCase @Inject constructor(private val repository: AssetRepository) {
    suspend operator fun invoke(asset: Asset): Long = repository.insert(asset)
}
