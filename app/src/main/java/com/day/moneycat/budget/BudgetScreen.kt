package com.day.moneycat.budget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.day.moneycat.transaction.MonthNavigator
import com.day.moneycat.transaction.formatWon
import com.moneycat.domain.model.Budget
import com.moneycat.ui.theme.ExpenseRed
import com.moneycat.ui.theme.IncomeGreen
import java.math.BigDecimal
import java.time.YearMonth

private val BUDGET_CATEGORIES = listOf("식비", "교통", "쇼핑", "의료", "문화", "주거", "통신", "기타")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen(
    onBack: () -> Unit,
    viewModel: BudgetViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showAddDialog by remember { mutableStateOf(false) }
    var editTarget by remember { mutableStateOf<Budget?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("예산 관리") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로")
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
            ) {
                Icon(Icons.Default.Add, contentDescription = "예산 추가")
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
        ) {
            MonthNavigator(
                label = "${state.yearMonth.year}년 ${state.yearMonth.monthValue}월",
                onPrev = viewModel::prevMonth,
                onNext = viewModel::nextMonth,
                canGoNext = state.yearMonth < YearMonth.now(),
            )

            if (state.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                return@Scaffold
            }

            if (state.items.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "예산을 설정해 보세요",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "우하단 + 버튼을 눌러 카테고리별 예산을 추가하세요",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                        )
                    }
                }
                return@Scaffold
            }

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(state.items, key = { it.budget.id }) { item ->
                    BudgetCard(
                        item = item,
                        onEdit = { editTarget = item.budget },
                        onDelete = { viewModel.delete(item.budget) },
                    )
                }
                item { Spacer(Modifier.height(72.dp)) }
            }
        }
    }

    if (showAddDialog || editTarget != null) {
        BudgetEditDialog(
            existing = editTarget,
            usedCategories = state.items.map { it.budget.category }.toSet(),
            yearMonth = state.yearMonth.toString(),
            onConfirm = { budget ->
                viewModel.upsert(budget)
                showAddDialog = false
                editTarget = null
            },
            onDismiss = {
                showAddDialog = false
                editTarget = null
            },
        )
    }
}

@Composable
private fun BudgetCard(
    item: BudgetWithSpending,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    val barColor = when {
        item.isOverBudget -> ExpenseRed
        item.isNearLimit -> androidx.compose.ui.graphics.Color(0xFFFF9800)
        else -> IncomeGreen
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    item.budget.category,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                )
                Row {
                    IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "수정",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        )
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "삭제",
                            modifier = Modifier.size(16.dp),
                            tint = ExpenseRed.copy(alpha = 0.7f),
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    formatWon(item.spent),
                    style = MaterialTheme.typography.bodyMedium,
                    color = barColor,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    "/ ${formatWon(item.budget.limitAmount)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                )
            }
            Spacer(Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { item.ratio.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = barColor,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
            Spacer(Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    "${(item.ratio * 100).toInt()}% 사용",
                    style = MaterialTheme.typography.labelSmall,
                    color = barColor,
                )
                Text(
                    "알림: ${(item.budget.alertThreshold * 100).toInt()}%",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BudgetEditDialog(
    existing: Budget?,
    usedCategories: Set<String>,
    yearMonth: String,
    onConfirm: (Budget) -> Unit,
    onDismiss: () -> Unit,
) {
    val availableCategories = if (existing != null)
        BUDGET_CATEGORIES
    else
        BUDGET_CATEGORIES.filter { it !in usedCategories }

    var selectedCategory by remember { mutableStateOf(existing?.category ?: availableCategories.firstOrNull() ?: "") }
    var limitText by remember { mutableStateOf(existing?.limitAmount?.toPlainString() ?: "") }
    var alertThreshold by remember { mutableFloatStateOf(existing?.alertThreshold ?: 0.8f) }
    var categoryExpanded by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (existing == null) "예산 추가" else "예산 수정") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                if (availableCategories.isEmpty() && existing == null) {
                    Text("모든 카테고리에 예산이 설정되어 있습니다.")
                    return@Column
                }

                Text("카테고리", style = MaterialTheme.typography.labelMedium)
                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = it },
                ) {
                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
                        singleLine = true,
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false },
                    ) {
                        availableCategories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = { selectedCategory = cat; categoryExpanded = false },
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = limitText,
                    onValueChange = { limitText = it; error = "" },
                    label = { Text("월 예산 한도") },
                    suffix = { Text("원") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = error.isNotEmpty(),
                    supportingText = if (error.isNotEmpty()) ({ Text(error) }) else null,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )

                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text("알림 기준", style = MaterialTheme.typography.labelMedium)
                        Text(
                            "${(alertThreshold * 100).toInt()}%",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Slider(
                        value = alertThreshold,
                        onValueChange = { alertThreshold = it },
                        valueRange = 0.5f..1.0f,
                        steps = 9,
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val amount = limitText.replace(",", "").toBigDecimalOrNull()
                if (amount == null || amount <= BigDecimal.ZERO) {
                    error = "올바른 금액을 입력해 주세요"
                    return@Button
                }
                onConfirm(
                    Budget(
                        id = existing?.id ?: 0,
                        category = selectedCategory,
                        yearMonth = yearMonth,
                        limitAmount = amount,
                        alertThreshold = alertThreshold,
                    )
                )
            }) { Text("저장") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("취소") }
        },
    )
}
