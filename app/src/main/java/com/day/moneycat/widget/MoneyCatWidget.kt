package com.day.moneycat.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.moneycat.domain.model.TransactionType
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.first
import java.math.BigDecimal
import java.text.NumberFormat
import java.time.YearMonth
import java.util.Locale

class MoneyCatWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val entryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            MoneyCatWidgetEntryPoint::class.java,
        )
        val transactionRepo = entryPoint.transactionRepository()
        val budgetRepo = entryPoint.budgetRepository()

        val ym = YearMonth.now()
        val transactions = transactionRepo.getByDateRange(ym.atDay(1), ym.atEndOfMonth()).first()
        val budgets = budgetRepo.getByMonth(ym.toString()).first()

        val totalExpense = transactions
            .filter { it.type == TransactionType.EXPENSE }
            .fold(BigDecimal.ZERO) { acc, t -> acc + t.amount }
        val totalBudget = budgets.fold(BigDecimal.ZERO) { acc, b -> acc + b.limitAmount }

        provideContent {
            WidgetContent(
                monthLabel = "${ym.monthValue}월 지출 현황",
                expense = totalExpense,
                budget = totalBudget,
            )
        }
    }
}

@Composable
private fun WidgetContent(
    monthLabel: String,
    expense: BigDecimal,
    budget: BigDecimal,
) {
    val remaining = budget - expense
    val hasBudget = budget > BigDecimal.ZERO

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(ColorProvider(Color(0xFF0F3460)))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = monthLabel,
            style = TextStyle(
                color = ColorProvider(Color(0xFFB0BEC5)),
                fontSize = 11.sp,
            ),
        )
        Spacer(GlanceModifier.height(4.dp))
        Text(
            text = formatWon(expense.toLong()),
            style = TextStyle(
                color = ColorProvider(Color.White),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            ),
        )
        if (hasBudget) {
            Spacer(GlanceModifier.height(6.dp))
            Text(
                text = if (remaining >= BigDecimal.ZERO)
                    "남은 예산 ${formatWon(remaining.toLong())}"
                else
                    "예산 ${formatWon((-remaining).toLong())} 초과",
                style = TextStyle(
                    color = ColorProvider(
                        if (remaining >= BigDecimal.ZERO) Color(0xFF4CAF50) else Color(0xFFFF5252),
                    ),
                    fontSize = 11.sp,
                ),
            )
        }
    }
}

private fun formatWon(amount: Long): String =
    "₩${NumberFormat.getNumberInstance(Locale.KOREA).format(amount)}"
