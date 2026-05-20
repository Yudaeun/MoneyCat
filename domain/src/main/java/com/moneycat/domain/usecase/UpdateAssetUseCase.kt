package com.moneycat.domain.usecase

import com.moneycat.domain.model.Asset
import com.moneycat.domain.repository.AssetRepository
import javax.inject.Inject

class UpdateAssetUseCase @Inject constructor(private val repository: AssetRepository) {
    suspend operator fun invoke(asset: Asset) = repository.update(asset)
}
