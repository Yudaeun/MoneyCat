package com.day.moneycat.card

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneycat.domain.model.Card
import com.moneycat.domain.model.CardBenefit
import com.moneycat.domain.model.CardType
import com.moneycat.domain.usecase.AddCardUseCase
import com.moneycat.domain.usecase.DeleteCardUseCase
import com.moneycat.domain.usecase.GetActiveCardsUseCase
import com.moneycat.domain.usecase.UpdateCardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.inject.Inject

data class CardListUiState(
    val cards: List<Card> = emptyList(),
    val isLoading: Boolean = true,
)

@HiltViewModel
class CardListViewModel @Inject constructor(
    getActiveCardsUseCase: GetActiveCardsUseCase,
    private val addCardUseCase: AddCardUseCase,
    private val updateCardUseCase: UpdateCardUseCase,
    private val deleteCardUseCase: DeleteCardUseCase,
) : ViewModel() {

    val uiState = getActiveCardsUseCase()
        .map { CardListUiState(cards = it, isLoading = false) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), CardListUiState())

    fun addCard(
        name: String,
        bankName: String,
        type: CardType,
        lastFourDigits: String?,
        annualFee: BigDecimal?,
        benefits: List<CardBenefit>,
    ) = viewModelScope.launch {
        addCardUseCase(
            Card(
                name = name,
                bankName = bankName,
                type = type,
                lastFourDigits = lastFourDigits.takeIf { !it.isNullOrBlank() },
                annualFee = annualFee,
                createdAt = LocalDateTime.now(),
            ),
            benefits,
        )
    }

    fun updateCard(
        existing: Card,
        name: String,
        bankName: String,
        type: CardType,
        lastFourDigits: String?,
        annualFee: BigDecimal?,
        benefits: List<CardBenefit>,
    ) = viewModelScope.launch {
        updateCardUseCase(
            existing.copy(
                name = name,
                bankName = bankName,
                type = type,
                lastFourDigits = lastFourDigits.takeIf { !it.isNullOrBlank() },
                annualFee = annualFee,
            ),
            benefits,
        )
    }

    fun deleteCard(cardId: Long) = viewModelScope.launch { deleteCardUseCase(cardId) }
}
