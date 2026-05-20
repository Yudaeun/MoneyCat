package com.day.moneycat.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moneycat.security.BiometricAuthManager
import com.moneycat.ui.composable.CatMascot
import com.moneycat.domain.model.CatExpression
import com.moneycat.ui.theme.BgLight
import com.moneycat.ui.theme.GrayMint
import com.moneycat.ui.theme.MintSage

@Composable
fun AuthScreen(
    onAuthenticated: () -> Unit,
    biometricAuthManager: BiometricAuthManager,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val activity = LocalContext.current as? FragmentActivity

    // FLAG_SECURE 적용
    SecureScreenEffect()

    // 생체인증 자동 시도
    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Authenticating && activity != null) {
            if (biometricAuthManager.canAuthenticate()) {
                biometricAuthManager.authenticate(
                    activity = activity,
                    onSuccess = { viewModel.onBiometricSuccess() },
                    onFailed = { viewModel.onBiometricFailed() },
                    onError = { _, _ -> viewModel.onBiometricError() }
                )
            } else {
                viewModel.onBiometricError()
            }
        }
    }

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Authenticated) {
            onAuthenticated()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgLight),
        contentAlignment = Alignment.Center
    ) {
        when (val state = uiState) {
            is AuthUiState.Authenticating -> AuthenticatingContent()
            is AuthUiState.PinSetup -> PinSetupContent(onPinSet = { viewModel.onPinSetup(it) })
            is AuthUiState.PinInput -> PinInputContent(onPinEntered = { viewModel.onPinEntered(it) })
            is AuthUiState.Locked -> LockedContent(remainingMinutes = state.remainingMinutes)
            is AuthUiState.Authenticated -> {}
        }
    }
}

@Composable
private fun AuthenticatingContent() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CatMascot(expression = CatExpression.DEFAULT, size = 120.dp)
        Text("머니캣", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("생체 인증으로 잠금을 해제하세요", color = Color.Gray)
    }
}

@Composable
private fun PinInputContent(onPinEntered: (String) -> Unit) {
    var pin by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.padding(horizontal = 32.dp)
    ) {
        CatMascot(expression = CatExpression.THINKING, size = 100.dp)
        Text("PIN 입력", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        PinDots(filledCount = pin.length, total = 6)
        PinKeypad(
            onDigit = {
                if (pin.length < 6) {
                    pin += it
                    if (pin.length == 6) {
                        onPinEntered(pin)
                        pin = ""
                    }
                }
            },
            onDelete = { if (pin.isNotEmpty()) pin = pin.dropLast(1) }
        )
    }
}

@Composable
private fun PinSetupContent(onPinSet: (String) -> Unit) {
    var pin by remember { mutableStateOf("") }
    var confirmPin by remember { mutableStateOf("") }
    var isConfirming by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.padding(horizontal = 32.dp)
    ) {
        CatMascot(expression = CatExpression.DEFAULT, size = 100.dp)
        Text(
            if (!isConfirming) "PIN 설정 (6자리)" else "PIN 확인",
            fontSize = 20.sp, fontWeight = FontWeight.SemiBold
        )
        if (errorMsg.isNotEmpty()) {
            Text(errorMsg, color = MaterialTheme.colorScheme.error, fontSize = 14.sp)
        }
        PinDots(filledCount = if (!isConfirming) pin.length else confirmPin.length, total = 6)
        PinKeypad(
            onDigit = {
                if (!isConfirming) {
                    if (pin.length < 6) {
                        pin += it
                        if (pin.length == 6) isConfirming = true
                    }
                } else {
                    if (confirmPin.length < 6) {
                        confirmPin += it
                        if (confirmPin.length == 6) {
                            if (pin == confirmPin) {
                                onPinSet(pin)
                            } else {
                                errorMsg = "PIN이 일치하지 않습니다"
                                pin = ""; confirmPin = ""; isConfirming = false
                            }
                        }
                    }
                }
            },
            onDelete = {
                if (!isConfirming) {
                    if (pin.isNotEmpty()) pin = pin.dropLast(1)
                } else {
                    if (confirmPin.isNotEmpty()) confirmPin = confirmPin.dropLast(1)
                }
            }
        )
    }
}

@Composable
private fun LockedContent(remainingMinutes: Long) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CatMascot(expression = CatExpression.SLEEPING, size = 120.dp)
        Text("앱이 잠겼습니다", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        Text(
            "${remainingMinutes}분 후에 다시 시도해주세요",
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun PinDots(filledCount: Int, total: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        repeat(total) { index ->
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .then(
                        if (index < filledCount) {
                            Modifier.background(MintSage, CircleShape)
                        } else {
                            Modifier.border(2.dp, GrayMint, CircleShape)
                        }
                    )
            )
        }
    }
}

@Composable
private fun PinKeypad(onDigit: (String) -> Unit, onDelete: () -> Unit) {
    val keys = listOf("1","2","3","4","5","6","7","8","9","","0","⌫")
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        keys.chunked(3).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { key ->
                    if (key.isEmpty()) {
                        Spacer(Modifier.size(72.dp))
                    } else {
                        FilledTonalButton(
                            onClick = { if (key == "⌫") onDelete() else onDigit(key) },
                            modifier = Modifier.size(72.dp),
                            shape = CircleShape
                        ) {
                            Text(key, fontSize = 20.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SecureScreenEffect() {
    val activity = LocalContext.current as? android.app.Activity ?: return
    DisposableEffect(Unit) {
        activity.window.setFlags(
            android.view.WindowManager.LayoutParams.FLAG_SECURE,
            android.view.WindowManager.LayoutParams.FLAG_SECURE
        )
        onDispose {
            activity.window.clearFlags(android.view.WindowManager.LayoutParams.FLAG_SECURE)
        }
    }
}
