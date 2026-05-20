package com.day.moneycat.asset

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.day.moneycat.transaction.formatWon
import com.moneycat.domain.model.Asset
import com.moneycat.domain.model.AssetType
import java.math.BigDecimal

private fun AssetType.label() = when (this) {
    AssetType.CASH -> "현금"
    AssetType.DEPOSIT -> "예금"
    AssetType.SAVINGS -> "적금"
    AssetType.STOCK -> "주식"
    AssetType.FOREIGN_CURRENCY -> "외화"
    AssetType.OTHER -> "기타"
}

private fun AssetType.emoji() = when (this) {
    AssetType.CASH -> "💵"
    AssetType.DEPOSIT -> "🏦"
    AssetType.SAVINGS -> "💰"
    AssetType.STOCK -> "📈"
    AssetType.FOREIGN_CURRENCY -> "💱"
    AssetType.OTHER -> "🗂"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetScreen(
    onBack: () -> Unit,
    viewModel: AssetViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showAddDialog by remember { mutableStateOf(false) }
    var editTarget by remember { mutableStateOf<Asset?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("자산 관리") },
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
                Icon(Icons.Default.Add, contentDescription = "자산 추가")
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
        ) {
            // 총 자산 카드
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        "총 자산",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                    Text(
                        formatWon(state.totalBalance),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }

            when {
                state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                state.grouped.isEmpty() -> Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "자산을 추가해 보세요",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    )
                }
                else -> LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    AssetType.entries.forEach { type ->
                        val assetsOfType = state.grouped[type] ?: return@forEach
                        item(key = "header_$type") {
                            Text(
                                "${type.emoji()} ${type.label()}",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
                            )
                        }
                        items(assetsOfType, key = { it.id }) { asset ->
                            AssetItem(
                                asset = asset,
                                onEdit = { editTarget = asset },
                                onDelete = { viewModel.delete(asset) },
                            )
                        }
                    }
                    item { Spacer(Modifier.height(80.dp)) }
                }
            }
        }
    }

    if (showAddDialog || editTarget != null) {
        AssetEditDialog(
            existing = editTarget,
            onConfirm = { name, type, balance, description ->
                if (editTarget != null) {
                    viewModel.update(editTarget!!, name, type, balance, description)
                } else {
                    viewModel.insert(name, type, balance, description)
                }
                showAddDialog = false
                editTarget = null
            },
            onDismiss = { showAddDialog = false; editTarget = null },
        )
    }
}

@Composable
private fun AssetItem(asset: Asset, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(asset.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                asset.description?.takeIf { it.isNotBlank() }?.let {
                    Text(it, style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                }
            }
            Text(
                formatWon(asset.balance),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(Modifier.width(4.dp))
            IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.Edit, contentDescription = "수정",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
            }
            IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.Delete, contentDescription = "삭제",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AssetEditDialog(
    existing: Asset?,
    onConfirm: (String, AssetType, BigDecimal, String?) -> Unit,
    onDismiss: () -> Unit,
) {
    var name by remember { mutableStateOf(existing?.name ?: "") }
    var selectedType by remember { mutableStateOf(existing?.type ?: AssetType.DEPOSIT) }
    var balanceText by remember { mutableStateOf(existing?.balance?.toPlainString() ?: "") }
    var description by remember { mutableStateOf(existing?.description ?: "") }
    var typeExpanded by remember { mutableStateOf(false) }
    var nameError by remember { mutableStateOf(false) }
    var balanceError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (existing == null) "자산 추가" else "자산 수정") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it; nameError = false },
                    label = { Text("이름") },
                    placeholder = { Text("예: 신한은행 통장") },
                    isError = nameError,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )

                ExposedDropdownMenuBox(
                    expanded = typeExpanded,
                    onExpandedChange = { typeExpanded = it },
                ) {
                    OutlinedTextField(
                        value = "${selectedType.emoji()} ${selectedType.label()}",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("유형") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
                        singleLine = true,
                    )
                    ExposedDropdownMenu(
                        expanded = typeExpanded,
                        onDismissRequest = { typeExpanded = false },
                    ) {
                        AssetType.entries.forEach { type ->
                            DropdownMenuItem(
                                text = { Text("${type.emoji()} ${type.label()}") },
                                onClick = { selectedType = type; typeExpanded = false },
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = balanceText,
                    onValueChange = { balanceText = it; balanceError = false },
                    label = { Text("잔액") },
                    suffix = { Text("원") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = balanceError,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("메모 (선택)") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2,
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                nameError = name.isBlank()
                val balance = balanceText.replace(",", "").toBigDecimalOrNull()
                balanceError = balance == null
                if (!nameError && !balanceError) {
                    onConfirm(name.trim(), selectedType, balance!!, description.takeIf { it.isNotBlank() })
                }
            }) { Text("저장") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("취소") }
        },
    )
}
