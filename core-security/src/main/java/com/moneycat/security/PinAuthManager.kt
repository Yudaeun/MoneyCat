package com.moneycat.security

import javax.inject.Inject
import javax.inject.Singleton

sealed interface PinResult {
    data object Success : PinResult
    data object NotSet : PinResult
    data class Wrong(val remaining: Int) : PinResult
    data class Locked(val remainingMinutes: Long) : PinResult
}

@Singleton
class PinAuthManager @Inject constructor(
    private val cryptoManager: CryptoManager
) {
    companion object {
        private const val KEY_PIN_HASH = "pin_hash"
        private const val KEY_PIN_SALT = "pin_salt"
        private const val KEY_FAIL_COUNT = "pin_fail_count"
        private const val KEY_LOCK_UNTIL = "pin_lock_until"
        private const val MAX_ATTEMPTS = 10
        private const val LOCK_DURATION_MS = 30 * 60 * 1000L
    }

    fun isPinSet(): Boolean = cryptoManager.contains(KEY_PIN_HASH)

    fun setupPin(pin: String) {
        val salt = cryptoManager.generateSalt()
        val hash = cryptoManager.hashPin(pin, salt)
        cryptoManager.saveEncrypted(KEY_PIN_SALT, salt)
        cryptoManager.saveEncrypted(KEY_PIN_HASH, hash)
        resetFailCount()
    }

    fun verifyPin(pin: String): PinResult {
        if (!isPinSet()) return PinResult.NotSet

        val lockUntil = cryptoManager.getEncrypted(KEY_LOCK_UNTIL)?.toLongOrNull() ?: 0L
        val now = System.currentTimeMillis()
        if (now < lockUntil) {
            val remainingMinutes = (lockUntil - now + 59_000) / 60_000
            return PinResult.Locked(remainingMinutes)
        }

        val salt = cryptoManager.getEncrypted(KEY_PIN_SALT) ?: return PinResult.NotSet
        val storedHash = cryptoManager.getEncrypted(KEY_PIN_HASH) ?: return PinResult.NotSet
        val inputHash = cryptoManager.hashPin(pin, salt)

        return if (inputHash == storedHash) {
            resetFailCount()
            PinResult.Success
        } else {
            val failCount = incrementFailCount()
            if (failCount >= MAX_ATTEMPTS) {
                cryptoManager.saveEncrypted(KEY_LOCK_UNTIL, (now + LOCK_DURATION_MS).toString())
                PinResult.Locked(30)
            } else {
                PinResult.Wrong(remaining = MAX_ATTEMPTS - failCount)
            }
        }
    }

    fun isLocked(): Boolean {
        val lockUntil = cryptoManager.getEncrypted(KEY_LOCK_UNTIL)?.toLongOrNull() ?: 0L
        return System.currentTimeMillis() < lockUntil
    }

    fun getRemainingLockMinutes(): Long {
        val lockUntil = cryptoManager.getEncrypted(KEY_LOCK_UNTIL)?.toLongOrNull() ?: 0L
        val remaining = lockUntil - System.currentTimeMillis()
        return if (remaining > 0) (remaining + 59_000) / 60_000 else 0
    }

    private fun resetFailCount() {
        cryptoManager.saveEncrypted(KEY_FAIL_COUNT, "0")
        cryptoManager.removeEncrypted(KEY_LOCK_UNTIL)
    }

    private fun incrementFailCount(): Int {
        val current = cryptoManager.getEncrypted(KEY_FAIL_COUNT)?.toIntOrNull() ?: 0
        val next = current + 1
        cryptoManager.saveEncrypted(KEY_FAIL_COUNT, next.toString())
        return next
    }
}
