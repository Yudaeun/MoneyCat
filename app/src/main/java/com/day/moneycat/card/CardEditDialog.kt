package com.day.moneycat.card

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.moneycat.domain.model.BenefitType
import com.moneycat.domain.model.Card
import com.moneycat.domain.model.CardBenefit
import com.moneycat.domain.model.CardType
import java.math.BigDecimal

private fun CardType.label() = when (this) {
    CardType.CREDIT -> "신용카드"
    CardType.DEBIT -> "체크카드"
    CardType.PREPAID -> "선불카드"
}

private fun BenefitType.label() = when (this) {
    BenefitType.DISCOUNT -> "할인"
    BenefitType.CASHBACK -> "캐시백"
    BenefitType.POINT -> "포인트"
}

private data class BenefitInput(
    val category: String = "",
    val benefitType: BenefitType = BenefitType.DISCOUNT,
    val discountRate: String = "",
    val description: String = "",
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardEditDialog(
    existing: Card?,
    onConfirm: (
        name: String,
        bankName: String,
        type: CardType,
        lastFourDigits: String?,
        annualFee: BigDecimal?,
        benefits: List<CardBenefit>,
    ) -> Unit,
    onDismiss: () -> Unit,
) {
    var name by remember { mutableStateOf(existing?.name ?: "") }
    var bankName by remember { mutableStateOf(existing?.bankName ?: "") }
    var selectedType by remember { mutableStateOf(existing?.type ?: CardType.CREDIT) }
    var lastFourDigits by remember { mutableStateOf(existing?.lastFourDigits ?: "") }
    var annualFeeText by remember { mutableStateOf(existing?.annualFee?.toPlainString() ?: "") }
    var typeExpanded by remember { mutableStateOf(false) }
    var benefits by remember { mutableStateOf(listOf(BenefitInput())) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (existing == null) "카드 추가" else "카드 수정") },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("카드명") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
                OutlinedTextField(
                    value = bankName,
                    onValueChange = { bankName = it },
                    label = { Text("은행/카드사") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )

                ExposedDropdownMenuBox(
                    expanded = typeExpanded,
                    onExpandedChange = { typeExpanded = it },
                ) {
                    OutlinedTextField(
                        value = selectedType.label(),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("카드 종류") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(typeExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                    )
                    ExposedDropdownMenu(
                        expanded = typeExpanded,
                        onDismissRequest = { typeExpanded = false },
                    ) {
                        CardType.entries.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type.label()) },
                                onClick = { selectedType = type; typeExpanded = false },
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = lastFourDigits,
                    onValueChange = { if (it.length <= 4) lastFourDigits = it },
                    label = { Text("끝 4자리 (선택)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
                OutlinedTextField(
                    value = annualFeeText,
                    onValueChange = { annualFeeText = it },
                    label = { Text("연회비 (선택, 원)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )

                Spacer(Modifier.height(4.dp))
                Text("혜택 정보", style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary)

                benefits.forEachIndexed { index, benefit ->
                    BenefitInputRow(
                        benefit = benefit,
                        showDelete = benefits.size > 1,
                        onUpdate = { updated ->
                            benefits = benefits.toMutableList().also { it[index] = updated }
                        },
                        onDelete = {
                            benefits = benefits.toMutableList().also { it.removeAt(index) }
                        },
                    )
                }
                if (benefits.size < 3) {
                    TextButton(
                        onClick = { benefits = benefits + BenefitInput() },
                        modifier = Modifier.align(Alignment.End),
                    ) {
                        Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("혜택 추가")
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isBlank() || bankName.isBlank()) return@TextButton
                    val parsedBenefits = benefits
                        .filter { it.category.isNotBlank() && it.description.isNotBlank() }
                        .map { b ->
                            CardBenefit(
                                category = b.category,
                                benefitType = b.benefitType,
                                discountRate = b.discountRate.toBigDecimalOrNull()
                                    ?.divide(BigDecimal("100")),
                                description = b.description,
                            )
                        }
                    onConfirm(
                        name.trim(),
                        bankName.trim(),
                        selectedType,
                        lastFourDigits.trim().takeIf { it.isNotBlank() },
                        annualFeeText.toBigDecimalOrNull(),
                        parsedBenefits,
                    )
                },
            ) { Text("저장") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("취소") }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BenefitInputRow(
    benefit: BenefitInput,
    showDelete: Boolean,
    onUpdate: (BenefitInput) -> Unit,
    onDelete: () -> Unit,
) {
    var typeExpanded by remember { mutableStateOf(false) }
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        ),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = benefit.category,
                    onValueChange = { onUpdate(benefit.copy(category = it)) },
                    label = { Text("카테고리") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                )
                if (showDelete) {
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, "삭제",
                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f))
                    }
                }
            }
            ExposedDropdownMenuBox(
                expanded = typeExpanded,
                onExpandedChange = { typeExpanded = it },
            ) {
                OutlinedTextField(
                    value = benefit.benefitType.label(),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("혜택 종류") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(typeExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                )
                ExposedDropdownMenu(
                    expanded = typeExpanded,
                    onDismissRequest = { typeExpanded = false },
                ) {
                    BenefitType.entries.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.label()) },
                            onClick = { onUpdate(benefit.copy(benefitType = type)); typeExpanded = false },
                        )
                    }
                }
            }
            if (benefit.benefitType == BenefitType.DISCOUNT) {
                OutlinedTextField(
                    value = benefit.discountRate,
                    onValueChange = { onUpdate(benefit.copy(discountRate = it)) },
                    label = { Text("할인율 (%)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
            }
            OutlinedTextField(
                value = benefit.description,
                onValueChange = { onUpdate(benefit.copy(description = it)) },
                label = { Text("혜택 설명") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
        }
    }
}
