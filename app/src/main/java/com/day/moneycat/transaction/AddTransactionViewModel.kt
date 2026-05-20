package com.day.moneycat.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneycat.domain.model.Currency
import com.moneycat.domain.model.InputSource
import com.moneycat.domain.model.PaymentMethod
import com.moneycat.domain.model.Transaction
import com.moneycat.domain.model.TransactionType
import com.moneycat.domain.usecase.AddTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

data class AddTransactionUiState(
    val type: TransactionType = TransactionType.EXPENSE,
    val amount: String = "",
    val category: String = "",
    val date: LocalDate = LocalDate.now(),
    val paymentMethod: PaymentMethod = PaymentMethod.CASH,
    val description: String = "",
    val isSaved: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val addTransactionUseCase: AddTransactionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddTransactionUiState())
    val uiState = _uiState.asStateFlow()

    fun setType(type: TransactionType) = _uiState.update { it.copy(type = type, category = "") }
    fun setAmount(amount: String) = _uiState.update { it.copy(amount = amount, errorMessage = null) }
    fun setCategory(category: String) = _uiState.update { it.copy(category = category) }
    fun setDate(date: LocalDate) = _uiState.update { it.copy(date = date) }
    fun setPaymentMethod(method: PaymentMethod) = _uiState.update { it.copy(paymentMethod = method) }
    fun setDescription(desc: String) = _uiState.update { it.copy(description = desc) }

    fun save() {
        val state = _uiState.value
        val parsed = state.amount.replace(",", "").toBigDecimalOrNull()
        if (parsed == null || parsed <= BigDecimal.ZERO) {
            _uiState.update { it.copy(errorMessage = "금액을 올바르게 입력해주세요") }
            return
        }
        if (state.category.isBlank()) {
            _uiState.update { it.copy(errorMessage = "카테고리를 선택해주세요") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            addTransactionUseCase(
                Transaction(
                    type = state.type,
                    amount = parsed,
                    currency = Currency.KRW,
                    category = state.category,
                    description = state.description.takeIf { it.isNotBlank() },
                    date = state.date,
                    paymentMethod = state.paymentMethod,
                    source = InputSource.MANUAL,
                    createdAt = LocalDateTime.now()
                )
            )
            _uiState.update { it.copy(isLoading = false, isSaved = true) }
        }
    }
}
