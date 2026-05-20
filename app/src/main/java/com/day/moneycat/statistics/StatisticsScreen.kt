package com.day.moneycat.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.day.moneycat.transaction.MonthNavigator
import com.day.moneycat.transaction.formatWon
import com.moneycat.domain.model.CategorySummary
import com.moneycat.ui.theme.ExpenseRed
import com.moneycat.ui.theme.IncomeGreen
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import java.math.BigDecimal
import java.time.YearMonth

@Composable
fun StatisticsScreen(
    onNavigateToBudget: () -> Unit = {},
    viewModel: StatisticsViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
    ) {
        MonthNavigator(
            label = "${state.yearMonth.year}년 ${state.yearMonth.monthValue}월",
            onPrev = viewModel::prevMonth,
            onNext = viewModel::nextMonth,
            canGoNext = state.yearMonth < YearMonth.now(),
        )

        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth().height(300.dp),
                contentAlignment = Alignment.Center,
            ) { CircularProgressIndicator() }
            return@Column
        }

        IncomeExpenseSummaryCard(income = state.totalIncome, expense = state.totalExpense)

        if (state.monthlyTrend.isNotEmpty()) {
            Spacer(Modifier.height(16.dp))
            MonthlyTrendSection(trend = state.monthlyTrend)
        }

        Spacer(Modifier.height(16.dp))

        if (state.expenseCategories.isNotEmpty()) {
            CategorySection(
                categories = state.expenseCategories,
                totalExpense = state.totalExpense,
            )
        } else {
            Box(
                modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    "이번 달 지출 내역이 없습니다",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                )
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun IncomeExpenseSummaryCard(income: BigDecimal, expense: BigDecimal) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SummaryItem(label = "수입", amount = income, color = IncomeGreen)
            VerticalDivider(modifier = Modifier.height(48.dp))
            SummaryItem(label = "지출", amount = expense, color = ExpenseRed)
            VerticalDivider(modifier = Modifier.height(48.dp))
            SummaryItem(
                label = "잔액",
                amount = income - expense,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun SummaryItem(
    label: String,
    amount: BigDecimal,
    color: androidx.compose.ui.graphics.Color,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
        )
        Spacer(Modifier.height(4.dp))
        Text(
            formatWon(amount),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = color,
        )
    }
}

@Composable
private fun MonthlyTrendSection(trend: List<Pair<YearMonth, BigDecimal>>) {
    val modelProducer = remember { CartesianChartModelProducer() }

    LaunchedEffect(trend) {
        modelProducer.runTransaction {
            lineSeries { series(trend.map { it.second.toFloat() }) }
        }
    }

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            "최근 6개월 지출 추이",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            trend.forEach { (ym, _) ->
                Text(
                    "${ym.monthValue}월",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                )
            }
        }
        Spacer(Modifier.height(4.dp))
        CartesianChartHost(
            chart = rememberCartesianChart(rememberLineCartesianLayer()),
            modelProducer = modelProducer,
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp),
        )
    }
}

@Composable
private fun CategorySection(
    categories: List<CategorySummary>,
    totalExpense: BigDecimal,
) {
    val modelProducer = remember { CartesianChartModelProducer() }

    LaunchedEffect(categories) {
        modelProducer.runTransaction {
            columnSeries { series(categories.map { it.total.toFloat() }) }
        }
    }

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            "카테고리별 지출",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.height(12.dp))

        CartesianChartHost(
            chart = rememberCartesianChart(rememberColumnCartesianLayer()),
            modelProducer = modelProducer,
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
        )

        Spacer(Modifier.height(16.dp))

        categories.forEach { cat ->
            CategoryProgressRow(
                category = cat.category,
                amount = cat.total,
                totalExpense = totalExpense,
            )
            Spacer(Modifier.height(10.dp))
        }
    }
}

@Composable
private fun CategoryProgressRow(
    category: String,
    amount: BigDecimal,
    totalExpense: BigDecimal,
) {
    val progress = if (totalExpense > BigDecimal.ZERO)
        (amount.toFloat() / totalExpense.toFloat()).coerceIn(0f, 1f)
    else 0f

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(category, style = MaterialTheme.typography.bodyMedium)
            Text(
                "${(progress * 100).toInt()}%  ${formatWon(amount)}",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = ExpenseRed,
            )
        }
        Spacer(Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(7.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = ExpenseRed.copy(alpha = 0.6f + progress * 0.4f),
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}
