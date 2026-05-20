package com.day.moneycat.insight

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moneycat.domain.model.AiInsight
import com.moneycat.domain.model.CatExpression
import com.moneycat.domain.model.InsightType
import com.moneycat.ui.composable.CatMascot
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun AiInsightScreen(
    viewModel: AiInsightViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val unreadCount = state.insights.count { !it.isRead }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            InsightHeader(
                unreadCount = unreadCount,
                isGenerating = state.isGenerating,
                onGenerate = viewModel::generate,
            )
        }

        if (state.insights.isEmpty() && !state.isGenerating) {
            item { EmptyState() }
        }

        items(state.insights, key = { it.id }) { insight ->
            InsightCard(
                insight = insight,
                onRead = { viewModel.markAsRead(insight.id) },
            )
        }
    }
}

@Composable
private fun InsightHeader(
    unreadCount: Int,
    isGenerating: Boolean,
    onGenerate: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            Text(
                "AI 소비 분석",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            if (unreadCount > 0) {
                Text(
                    "읽지 않은 인사이트 ${unreadCount}건",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            } else {
                Text(
                    "이번 달 소비 패턴을 분석해드려요",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                )
            }
        }
        Button(
            onClick = onGenerate,
            enabled = !isGenerating,
            shape = RoundedCornerShape(12.dp),
        ) {
            if (isGenerating) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp,
                )
            } else {
                Icon(Icons.Filled.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("분석하기")
            }
        }
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CatMascot(expression = CatExpression.THINKING, size = 80.dp)
            Spacer(Modifier.height(16.dp))
            Text(
                "아직 인사이트가 없어요",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "'분석하기' 버튼을 눌러 소비 패턴을 분석해보세요",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
            )
        }
    }
}

@Composable
private fun InsightCard(
    insight: AiInsight,
    onRead: () -> Unit,
) {
    val style = insightStyle(insight.type)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !insight.isRead) { onRead() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = if (!insight.isRead) BorderStroke(1.dp, style.tint.copy(alpha = 0.4f)) else null,
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(style.background),
                contentAlignment = Alignment.Center,
            ) {
                Icon(style.icon, contentDescription = null, tint = style.tint, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        insight.title,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f),
                    )
                    if (!insight.isRead) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                        )
                    }
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    insight.content,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                )
                insight.estimatedSaving?.let { saving ->
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "예상 절약액: ${formatWon(saving.toLong())}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    insight.createdAt.format(DateTimeFormatter.ofPattern("M.d HH:mm")),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    fontSize = 10.sp,
                )
            }
        }
    }
}

private data class InsightStyle(val icon: ImageVector, val tint: Color, val background: Color)

private fun insightStyle(type: InsightType) = when (type) {
    InsightType.SPENDING_ALERT -> InsightStyle(
        icon = Icons.Filled.Warning,
        tint = Color(0xFFFF6B35),
        background = Color(0xFFFF6B35).copy(alpha = 0.15f),
    )
    InsightType.SAVING_TIP -> InsightStyle(
        icon = Icons.Filled.Savings,
        tint = Color(0xFF2ECC71),
        background = Color(0xFF2ECC71).copy(alpha = 0.15f),
    )
    InsightType.CARD_SUGGESTION -> InsightStyle(
        icon = Icons.Filled.CreditCard,
        tint = Color(0xFF3498DB),
        background = Color(0xFF3498DB).copy(alpha = 0.15f),
    )
    InsightType.ANOMALY -> InsightStyle(
        icon = Icons.Filled.Error,
        tint = Color(0xFFE74C3C),
        background = Color(0xFFE74C3C).copy(alpha = 0.15f),
    )
    InsightType.WEEKLY_SUMMARY -> InsightStyle(
        icon = Icons.Filled.BarChart,
        tint = Color(0xFF9B59B6),
        background = Color(0xFF9B59B6).copy(alpha = 0.15f),
    )
}

private fun formatWon(amount: Long): String {
    val nf = NumberFormat.getNumberInstance(Locale.KOREA)
    return "₩${nf.format(amount)}"
}
