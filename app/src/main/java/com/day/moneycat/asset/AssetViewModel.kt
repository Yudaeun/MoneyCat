package com.day.moneycat.asset

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneycat.domain.model.Asset
import com.moneycat.domain.model.AssetType
import com.moneycat.domain.model.Currency
import com.moneycat.domain.repository.AssetRepository
import com.moneycat.domain.usecase.DeleteAssetUseCase
import com.moneycat.domain.usecase.InsertAssetUseCase
import com.moneycat.domain.usecase.UpdateAssetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.inject.Inject

data class AssetUiState(
    val grouped: Map<AssetType, List<Asset>> = emptyMap(),
    val totalBalance: BigDecimal = BigDecimal.ZERO,
    val isLoading: Boolean = true,
)

@HiltViewModel
class AssetViewModel @Inject constructor(
    private val assetRepository: AssetRepository,
    private val insertAssetUseCase: InsertAssetUseCase,
    private val updateAssetUseCase: UpdateAssetUseCase,
    private val deleteAssetUseCase: DeleteAssetUseCase,
) : ViewModel() {

    val uiState = assetRepository.getAll()
        .map { assets ->
            AssetUiState(
                grouped = assets.groupBy { it.type },
                totalBalance = assets.fold(BigDecimal.ZERO) { acc, a -> acc + a.balance },
                isLoading = false,
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AssetUiState(),
        )

    fun insert(
        name: String,
        type: AssetType,
        balance: BigDecimal,
        description: String?,
    ) = viewModelScope.launch {
        insertAssetUseCase(
            Asset(
                name = name,
                type = type,
                currency = Currency.KRW,
                balance = balance,
                description = description,
                updatedAt = LocalDateTime.now(),
            )
        )
    }

    fun update(asset: Asset, name: String, type: AssetType, balance: BigDecimal, description: String?) =
        viewModelScope.launch {
            updateAssetUseCase(
                asset.copy(
                    name = name,
                    type = type,
                    balance = balance,
                    description = description,
                    updatedAt = LocalDateTime.now(),
                )
            )
        }

    fun delete(asset: Asset) = viewModelScope.launch { deleteAssetUseCase(asset) }
}
