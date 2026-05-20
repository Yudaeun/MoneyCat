package com.day.moneycat.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moneycat.domain.model.AiInsight
import com.moneycat.domain.model.CatExpression
import com.moneycat.domain.model.InsightType
import com.moneycat.domain.model.Transaction
import com.moneycat.domain.model.TransactionType
import com.moneycat.ui.composable.CatMascot
import com.moneycat.ui.theme.ExpenseRed
import com.moneycat.ui.theme.IncomeGreen
import java.math.BigDecimal
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToAsset: () -> Unit = {},
    onNavigateToInsights: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item { CatHeader(expression = uiState.catExpression, userName = uiState.userName) }
        item { SummaryCard(uiState) }
        item { AssetCard(uiState.totalAssets, onClick = onNavigateToAsset) }
        uiState.latestInsight?.let { insight ->
            item { InsightPreviewCard(insight = insight, onClick = onNavigateToInsights) }
        }
        if (uiState.recentTransactions.isNotEmpty()) {
            item {
                Text(
                    text = "최근 거래",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
            items(uiState.recentTransactions) { tx ->
                TransactionItem(tx)
            }
        }
    }
}

@Composable
private fun CatHeader(expression: CatExpression, userName: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CatMascot(expression = expression, size = 100.dp)
        Spacer(Modifier.height(8.dp))
        if (userName.isNotBlank()) {
            Text(
                text = "안녕하세요, ${userName}님!",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(Modifier.height(2.dp))
        }
        Text(
            text = catMessage(expression),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
        )
    }
}

@Composable
private fun InsightPreviewCard(insight: AiInsight, onClick: () -> Unit) {
    val tint = insightTint(insight.type)
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = tint.copy(alpha = 0.12f)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Filled.AutoAwesome,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(20.dp),
            )
            Spacer(Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    insight.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = tint,
                )
                Text(
                    insight.content.lines().first(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                    maxLines = 1,
                )
            }
            Text(
                "더 보기 >",
                style = MaterialTheme.typography.labelSmall,
                color = tint,
            )
        }
    }
}

@Composable
private fun SummaryCard(state: HomeUiState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = monthLabel(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                AmountItem(label = "수입", amount = state.totalIncome, color = IncomeGreen)
                AmountItem(label = "지출", amount = state.totalExpense, color = ExpenseRed)
                AmountItem(
                    label = "잔액",
                    amount = state.totalIncome - state.totalExpense,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            state.budgetLimit?.let { limit ->
                val progress = if (limit > BigDecimal.ZERO)
                    (state.totalExpense.toFloat() / limit.toFloat()).coerceIn(0f, 1f)
                else 0f
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "예산 ${formatWon(state.totalExpense)} / ${formatWon(limit)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                )
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(4.dp)),
                    color = if (progress >= 1f) ExpenseRed else IncomeGreen,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun AmountItem(label: String, amount: BigDecimal, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
        Text(text = formatWon(amount), style = MaterialTheme.typography.bodyLarge, color = color, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun AssetCard(totalAssets: BigDecimal, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "총 자산",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            Text(
                text = formatWon(totalAssets),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }
}

@Composable
private fun TransactionItem(transaction: Transaction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    if (transaction.type == TransactionType.INCOME) IncomeGreen.copy(alpha = 0.2f)
                    else ExpenseRed.copy(alpha = 0.2f)
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = categoryEmoji(transaction.category), fontSize = 18.sp)
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = transaction.category, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
            transaction.description?.takeIf { it.isNotBlank() }?.let {
                Text(text = it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = if (transaction.type == TransactionType.INCOME) "+${formatWon(transaction.amount)}"
                       else "-${formatWon(transaction.amount)}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = if (transaction.type == TransactionType.INCOME) IncomeGreen else ExpenseRed,
            )
            Text(
                text = transaction.date.format(DateTimeFormatter.ofPattern("M.d")),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            )
        }
    }
}

private fun insightTint(type: InsightType) = when (type) {
    InsightType.SPENDING_ALERT -> Color(0xFFFF6B35)
    InsightType.SAVING_TIP -> Color(0xFF2ECC71)
    InsightType.CARD_SUGGESTION -> Color(0xFF3498DB)
    InsightType.ANOMALY -> Color(0xFFE74C3C)
    InsightType.WEEKLY_SUMMARY -> Color(0xFF9B59B6)
}

private fun catMessage(expression: CatExpression) = when (expression) {
    CatExpression.HAPPY -> "이번 달 절약 잘하고 있어요!"
    CatExpression.ALERT -> "예산을 초과했어요!"
    CatExpression.SLEEPING -> "오랫동안 기록이 없어요..."
    CatExpression.THINKING -> "지출을 분석 중이에요..."
    CatExpression.DEFAULT -> "오늘도 가계부 써요!"
}

private fun monthLabel(): String {
    val now = LocalDate.now()
    return "${now.year}년 ${now.monthValue}월"
}

private fun formatWon(amount: BigDecimal): String {
    val nf = NumberFormat.getNumberInstance(Locale.KOREA)
    return "₩${nf.format(amount)}"
}

private fun categoryEmoji(category: String) = when (category) {
    "식비" -> "🍚"
    "교통" -> "🚌"
    "쇼핑" -> "🛍"
    "의료" -> "💊"
    "문화" -> "🎬"
    "주거" -> "🏠"
    "통신" -> "📱"
    "급여" -> "💰"
    "부업" -> "💼"
    "투자" -> "📈"
    "용돈" -> "🎁"
    else -> "💸"
}
