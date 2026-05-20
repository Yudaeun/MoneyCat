package com.day.moneycat.asset

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneycat.domain.model.Asset
import com.moneycat.domain.model.AssetType
import com.moneycat.domain.model.Currency
import com.moneycat.domain.repository.AssetRepository
import com.moneycat.domain.usecase.DeleteAssetUseCase
import com.moneycat.domain.usecase.GetExchangeRatesUseCase
import com.moneycat.domain.usecase.InsertAssetUseCase
import com.moneycat.domain.usecase.RefreshExchangeRatesUseCase
import com.moneycat.domain.usecase.UpdateAssetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.inject.Inject

data class AssetUiState(
    val grouped: Map<AssetType, List<Asset>> = emptyMap(),
    val totalBalance: BigDecimal = BigDecimal.ZERO,
    val exchangeRates: Map<String, BigDecimal> = emptyMap(),
    val isLoading: Boolean = true,
)

@HiltViewModel
class AssetViewModel @Inject constructor(
    private val assetRepository: AssetRepository,
    private val insertAssetUseCase: InsertAssetUseCase,
    private val updateAssetUseCase: UpdateAssetUseCase,
    private val deleteAssetUseCase: DeleteAssetUseCase,
    private val getExchangeRatesUseCase: GetExchangeRatesUseCase,
    private val refreshExchangeRatesUseCase: RefreshExchangeRatesUseCase,
) : ViewModel() {

    val uiState = combine(
        assetRepository.getAll(),
        getExchangeRatesUseCase(),
    ) { assets, rates ->
        val rateMap = rates.associate { r ->
            val code = r.currencyPair.substringBefore("/")
            code to r.rate
        }
        val totalKrw = assets.fold(BigDecimal.ZERO) { acc, a ->
            val krwRate = if (a.currency == Currency.KRW) BigDecimal.ONE
            else rateMap[a.currency.name] ?: BigDecimal.ZERO
            acc + a.balance.multiply(krwRate)
        }
        AssetUiState(
            grouped = assets.groupBy { it.type },
            totalBalance = totalKrw,
            exchangeRates = rateMap,
            isLoading = false,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AssetUiState(),
    )

    init {
        viewModelScope.launch {
            val rates = getExchangeRatesUseCase().first()
            refreshExchangeRatesUseCase(rates)
        }
    }

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
