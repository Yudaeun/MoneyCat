package com.day.moneycat.transaction

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moneycat.domain.model.PaymentMethod
import com.moneycat.domain.model.Transaction
import com.moneycat.domain.model.TransactionType
import com.moneycat.ui.theme.ExpenseRed
import com.moneycat.ui.theme.IncomeGreen
import java.math.BigDecimal
import java.text.NumberFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionListScreen(
    viewModel: TransactionListViewModel = hiltViewModel(),
    onEditTransaction: (Long) -> Unit = {},
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var isSearchVisible by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        MonthNavigator(
            label = "${state.yearMonth.year}년 ${state.yearMonth.monthValue}월",
            onPrev = viewModel::prevMonth,
            onNext = viewModel::nextMonth,
            canGoNext = state.yearMonth < YearMonth.now(),
            isSearchActive = isSearchVisible,
            onToggleSearch = {
                isSearchVisible = !isSearchVisible
                if (!isSearchVisible) viewModel.setSearchQuery("")
            },
        )

        AnimatedVisibility(
            visible = isSearchVisible,
            enter = expandVertically(),
            exit = shrinkVertically(),
        ) {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = viewModel::setSearchQuery,
                placeholder = { Text("카테고리 또는 메모 검색") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                trailingIcon = {
                    if (state.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setSearchQuery("") }) {
                            Icon(Icons.Filled.Close, contentDescription = "검색어 지우기")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
            )
        }

        FilterTabs(selected = state.filter, onSelect = viewModel::setFilter)

        when {
            state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            state.transactions.isEmpty() -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        if (state.searchQuery.isNotBlank()) "검색 결과가 없습니다"
                        else "이번 달 거래 내역이 없습니다",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    )
                }
            }
            else -> {
                MonthlySummaryRow(income = state.totalIncome, expense = state.totalExpense)
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                val grouped = state.transactions
                    .groupBy { it.date }
                    .entries.sortedByDescending { it.key }

                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    grouped.forEach { (date, txList) ->
                        item(key = "date_$date") { DateHeader(date) }
                        items(txList, key = { it.id }) { tx ->
                            SwipeToDeleteItem(
                                transaction = tx,
                                onDelete = { viewModel.delete(tx) },
                                onClick = { onEditTransaction(tx.id) },
                            )
                        }
                    }
                    item { Spacer(Modifier.height(8.dp)) }
                }
            }
        }
    }
}

@Composable
internal fun MonthNavigator(
    label: String,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    canGoNext: Boolean,
    isSearchActive: Boolean = false,
    onToggleSearch: (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        IconButton(onClick = onPrev) {
            Icon(Icons.AutoMirrored.Filled.ArrowBackIos, contentDescription = "이전 달", modifier = Modifier.size(20.dp))
        }
        Text(label, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Row {
            if (onToggleSearch != null) {
                IconButton(onClick = onToggleSearch) {
                    Icon(
                        if (isSearchActive) Icons.Filled.Close else Icons.Filled.Search,
                        contentDescription = if (isSearchActive) "검색 닫기" else "검색",
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
            IconButton(onClick = onNext, enabled = canGoNext) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = "다음 달",
                    modifier = Modifier.size(20.dp),
                    tint = if (canGoNext) MaterialTheme.colorScheme.onSurface
                           else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                )
            }
        }
    }
}

@Composable
private fun FilterTabs(selected: TransactionFilter, onSelect: (TransactionFilter) -> Unit) {
    val labels = mapOf(
        TransactionFilter.ALL to "전체",
        TransactionFilter.INCOME to "수입",
        TransactionFilter.EXPENSE to "지출",
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        TransactionFilter.entries.forEach { filter ->
            FilterChip(
                selected = selected == filter,
                onClick = { onSelect(filter) },
                label = { Text(labels[filter] ?: "") },
            )
        }
    }
}

@Composable
private fun MonthlySummaryRow(income: BigDecimal, expense: BigDecimal) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        SummaryColumn("수입", income, IncomeGreen)
        SummaryColumn("지출", expense, ExpenseRed)
        SummaryColumn("합계", income - expense, MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun SummaryColumn(label: String, amount: BigDecimal, color: androidx.compose.ui.graphics.Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
        )
        Text(
            formatWon(amount),
            color = color,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
        )
    }
}

@Composable
private fun DateHeader(date: LocalDate) {
    Text(
        text = date.format(DateTimeFormatter.ofPattern("M월 d일 (E)")),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f),
        modifier = Modifier.padding(top = 12.dp, bottom = 4.dp),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeToDeleteItem(
    transaction: Transaction,
    onDelete: () -> Unit,
    onClick: () -> Unit,
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
                true
            } else false
        },
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            val color by animateColorAsState(
                targetValue = if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart)
                    ExpenseRed else MaterialTheme.colorScheme.surface,
                label = "swipe_bg",
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp))
                    .background(color),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "삭제",
                    tint = Color.White,
                    modifier = Modifier.padding(end = 20.dp),
                )
            }
        },
    ) {
        TransactionListItem(transaction = transaction, onClick = onClick)
    }
}

@Composable
private fun TransactionListItem(transaction: Transaction, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(
                    if (transaction.type == TransactionType.INCOME)
                        IncomeGreen.copy(alpha = 0.15f)
                    else ExpenseRed.copy(alpha = 0.15f),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(categoryEmoji(transaction.category), fontSize = 16.sp)
        }
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text(
                transaction.category,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
            )
            transaction.description?.takeIf { it.isNotBlank() }?.let {
                Text(
                    it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                )
            }
            Text(
                transaction.paymentMethod.displayName(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
            )
        }
        Text(
            text = if (transaction.type == TransactionType.INCOME)
                "+${formatWon(transaction.amount)}"
            else "-${formatWon(transaction.amount)}",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = if (transaction.type == TransactionType.INCOME) IncomeGreen else ExpenseRed,
        )
    }
}

private fun PaymentMethod.displayName() = when (this) {
    PaymentMethod.CASH -> "현금"
    PaymentMethod.CARD -> "카드"
    PaymentMethod.TRANSFER -> "이체"
}

internal fun categoryEmoji(category: String) = when (category) {
    "식비" -> "🍚"; "교통" -> "🚌"; "쇼핑" -> "🛍"; "의료" -> "💊"
    "문화" -> "🎬"; "주거" -> "🏠"; "통신" -> "📱"; "급여" -> "💰"
    "부업" -> "💼"; "투자" -> "📈"; "용돈" -> "🎁"; else -> "💸"
}

internal fun formatWon(amount: BigDecimal): String =
    "₩${NumberFormat.getNumberInstance(Locale.KOREA).format(amount)}"
