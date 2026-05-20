package com.day.moneycat.settings

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moneycat.domain.model.CatExpression
import com.moneycat.ui.composable.CatMascot

@Composable
fun SettingsScreen(
    onNavigateToBudget: () -> Unit = {},
    onNavigateToAsset: () -> Unit = {},
    onNavigateToProfileEdit: () -> Unit = {},
    onNavigateToCardList: () -> Unit = {},
    onNavigateToNotificationRules: () -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val exportResult by viewModel.exportResult.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    var isNotificationListenerEnabled by remember {
        mutableStateOf(
            Settings.Secure.getString(context.contentResolver, "enabled_notification_listeners")
                ?.split(":")?.any { it.contains(context.packageName) } == true
        )
    }
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                isNotificationListenerEnabled = Settings.Secure.getString(
                    context.contentResolver, "enabled_notification_listeners"
                )?.split(":")?.any { it.contains(context.packageName) } == true
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(exportResult) {
        if (exportResult is ExportResult.Success) {
            context.startActivity(
                Intent.createChooser(
                    (exportResult as ExportResult.Success).shareIntent,
                    "CSV 내보내기",
                )
            )
            viewModel.clearExportResult()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
    ) {
        Spacer(Modifier.height(24.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CatMascot(expression = CatExpression.DEFAULT, size = 72.dp)
            Spacer(Modifier.height(8.dp))
            Text("MoneyCat", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(
                "v1.0.0",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            )
        }

        Spacer(Modifier.height(24.dp))
        HorizontalDivider()

        SectionHeader("예산 · 자산")
        SettingItem(title = "예산 관리", subtitle = "카테고리별 월 예산 설정", onClick = onNavigateToBudget)
        SettingItem(title = "자산 관리", subtitle = "계좌 · 현금 · 투자 잔액 관리", onClick = onNavigateToAsset)
        SettingItem(title = "카드 관리", subtitle = "보유 카드 및 혜택 정보 관리", onClick = onNavigateToCardList)

        SectionHeader("자동 입력")
        SettingItem(
            title = "알림 자동 입력",
            subtitle = "카드사별 규칙 관리 및 권한 설정",
            badge = if (isNotificationListenerEnabled) "활성화" else "설정 필요",
            badgeColor = if (isNotificationListenerEnabled)
                MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.error,
            onClick = onNavigateToNotificationRules,
        )

        SectionHeader("데이터")
        SettingItem(
            title = "CSV 내보내기",
            subtitle = "전체 거래 내역을 파일로 저장",
            isLoading = exportResult is ExportResult.Loading,
            onClick = { viewModel.exportCsv(context) },
        )
        if (exportResult is ExportResult.Error) {
            Text(
                "내보내기 실패. 다시 시도해 주세요.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 8.dp),
            )
        }

        SectionHeader("보안")
        SettingItem(title = "PIN 및 생체인증", subtitle = "잠금 방식 변경")
        SettingItem(title = "자동 잠금", subtitle = "10분 비활성 시 잠금")

        SectionHeader("앱 정보")
        SettingItem(title = "프로필 수정", subtitle = "이름 · 월 수입 · 재무 목표 변경", onClick = onNavigateToProfileEdit)
        SettingItem(title = "오픈소스 라이선스", subtitle = "사용된 라이브러리")
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        title,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 16.dp, bottom = 4.dp),
    )
}

@Composable
private fun SettingItem(
    title: String,
    subtitle: String,
    isLoading: Boolean = false,
    badge: String? = null,
    badgeColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            Text(
                subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f),
            )
        }
        if (badge != null) {
            Surface(
                shape = MaterialTheme.shapes.small,
                color = badgeColor.copy(alpha = 0.15f),
            ) {
                Text(
                    badge,
                    style = MaterialTheme.typography.labelSmall,
                    color = badgeColor,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                )
            }
            Spacer(Modifier.width(4.dp))
        }
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
        } else {
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
            )
        }
    }
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
}
