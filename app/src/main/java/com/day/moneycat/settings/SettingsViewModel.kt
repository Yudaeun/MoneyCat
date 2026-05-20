package com.day.moneycat.settings

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneycat.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
) : ViewModel() {

    private val _exportResult = MutableStateFlow<ExportResult?>(null)
    val exportResult = _exportResult.asStateFlow()

    fun exportCsv(context: Context) = viewModelScope.launch {
        _exportResult.value = ExportResult.Loading
        val transactions = transactionRepository.getAll().first()
        val uri = CsvExporter.export(context, transactions)
        if (uri != null) {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/csv"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            _exportResult.value = ExportResult.Success(shareIntent)
        } else {
            _exportResult.value = ExportResult.Error
        }
    }

    fun clearExportResult() { _exportResult.value = null }
}

sealed interface ExportResult {
    data object Loading : ExportResult
    data class Success(val shareIntent: Intent) : ExportResult
    data object Error : ExportResult
}
