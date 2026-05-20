package com.moneycat.security

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext context: Context
) {
    companion object {
        private const val PREFS_NAME = "session_prefs"
        private const val KEY_LAST_ACTIVE = "last_active_ms"
        const val SESSION_TIMEOUT_MS = 10 * 60 * 1000L
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun updateLastActive() {
        prefs.edit().putLong(KEY_LAST_ACTIVE, System.currentTimeMillis()).apply()
    }

    fun isSessionExpired(): Boolean {
        val lastActive = prefs.getLong(KEY_LAST_ACTIVE, 0L)
        if (lastActive == 0L) return true
        return System.currentTimeMillis() - lastActive > SESSION_TIMEOUT_MS
    }

    fun clearSession() {
        prefs.edit().remove(KEY_LAST_ACTIVE).apply()
    }
}
