package com.day.moneycat.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneycat.security.BiometricAuthManager
import com.moneycat.security.PinAuthManager
import com.moneycat.security.PinResult
import com.moneycat.security.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AuthUiState {
    data object Authenticating : AuthUiState
    data object PinSetup : AuthUiState
    data object PinInput : AuthUiState
    data class Locked(val remainingMinutes: Long) : AuthUiState
    data object Authenticated : AuthUiState
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val biometricAuthManager: BiometricAuthManager,
    private val pinAuthManager: PinAuthManager,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Authenticating)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private var failBiometricCount = 0

    init {
        checkInitialState()
    }

    private fun checkInitialState() {
        viewModelScope.launch {
            when {
                pinAuthManager.isLocked() -> {
                    _uiState.value = AuthUiState.Locked(pinAuthManager.getRemainingLockMinutes())
                }
                !pinAuthManager.isPinSet() -> {
                    _uiState.value = AuthUiState.PinSetup
                }
                sessionManager.isSessionExpired() -> {
                    _uiState.value = AuthUiState.Authenticating
                }
                else -> {
                    _uiState.value = AuthUiState.Authenticated
                }
            }
        }
    }

    fun onBiometricSuccess() {
        sessionManager.updateLastActive()
        _uiState.value = AuthUiState.Authenticated
    }

    fun onBiometricFailed() {
        failBiometricCount++
        if (failBiometricCount >= 5) {
            _uiState.value = AuthUiState.PinInput
        }
    }

    fun onBiometricError() {
        _uiState.value = AuthUiState.PinInput
    }

    fun onPinEntered(pin: String) {
        viewModelScope.launch {
            when (val result = pinAuthManager.verifyPin(pin)) {
                is PinResult.Success -> {
                    sessionManager.updateLastActive()
                    _uiState.value = AuthUiState.Authenticated
                }
                is PinResult.Wrong -> {
                    // UI에서 remaining 표시
                }
                is PinResult.Locked -> {
                    _uiState.value = AuthUiState.Locked(result.remainingMinutes)
                }
                is PinResult.NotSet -> {
                    _uiState.value = AuthUiState.PinSetup
                }
            }
        }
    }

    fun onPinSetup(pin: String) {
        pinAuthManager.setupPin(pin)
        _uiState.value = AuthUiState.Authenticating
    }

    fun checkSessionOnResume() {
        if (!sessionManager.isSessionExpired()) return
        if (pinAuthManager.isLocked()) {
            _uiState.value = AuthUiState.Locked(pinAuthManager.getRemainingLockMinutes())
        } else {
            _uiState.value = AuthUiState.Authenticating
        }
    }
}
