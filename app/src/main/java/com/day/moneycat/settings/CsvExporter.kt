package com.day.moneycat.settings

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.moneycat.domain.model.PaymentMethod
import com.moneycat.domain.model.Transaction
import com.moneycat.domain.model.TransactionType
import java.io.PrintWriter

object CsvExporter {

    fun export(context: Context, transactions: List<Transaction>): Uri? = runCatching {
        val fileName = "moneycat_${System.currentTimeMillis()}.csv"
        val values = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, fileName)
            put(MediaStore.Downloads.MIME_TYPE, "text/csv")
            put(MediaStore.Downloads.IS_PENDING, 1)
        }
        val uri = context.contentResolver.insert(
            MediaStore.Downloads.EXTERNAL_CONTENT_URI, values
        ) ?: return null

        context.contentResolver.openOutputStream(uri)?.use { stream ->
            PrintWriter(stream.writer(Charsets.UTF_8)).use { writer ->
                writer.println(csvRow("날짜", "유형", "카테고리", "금액", "결제수단", "메모"))
                transactions.sortedByDescending { it.date }.forEach { tx ->
                    writer.println(
                        csvRow(
                            tx.date.toString(),
                            if (tx.type == TransactionType.INCOME) "수입" else "지출",
                            tx.category,
                            tx.amount.toPlainString(),
                            tx.paymentMethod.csvLabel(),
                            tx.description ?: "",
                        )
                    )
                }
            }
        }

        values.clear()
        values.put(MediaStore.Downloads.IS_PENDING, 0)
        context.contentResolver.update(uri, values, null, null)
        uri
    }.getOrNull()

    private fun csvRow(vararg fields: String) =
        fields.joinToString(",") { field ->
            if (field.contains(',') || field.contains('"') || field.contains('\n'))
                "\"${field.replace("\"", "\"\"")}\""
            else field
        }

    private fun PaymentMethod.csvLabel() = when (this) {
        PaymentMethod.CASH -> "현금"
        PaymentMethod.CARD -> "카드"
        PaymentMethod.TRANSFER -> "이체"
    }
}
