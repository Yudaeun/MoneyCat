package com.moneycat.security

import android.content.Context
import android.content.SharedPreferences
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SessionManagerTest {

    private lateinit var sessionManager: SessionManager
    private val storage = mutableMapOf<String, Long>()

    @Before
    fun setUp() {
        val prefs = mockk<SharedPreferences>()
        val editor = mockk<SharedPreferences.Editor>()
        val keySlot = slot<String>()
        val valueSlot = slot<Long>()

        every { prefs.getLong(any(), any()) } answers { storage[firstArg()] ?: secondArg() }
        every { prefs.edit() } returns editor
        every { editor.putLong(capture(keySlot), capture(valueSlot)) } answers {
            storage[keySlot.captured] = valueSlot.captured; editor
        }
        every { editor.remove(any()) } answers { storage.remove(firstArg<String>()); editor }
        every { editor.apply() } returns Unit

        val context = mockk<Context>()
        every { context.getSharedPreferences(any(), any()) } returns prefs

        sessionManager = SessionManager(context)
    }

    @Test
    fun `마지막 활동 없음 → isSessionExpired() = true`() {
        assertTrue(sessionManager.isSessionExpired())
    }

    @Test
    fun `updateLastActive 직후 → isSessionExpired() = false`() {
        sessionManager.updateLastActive()
        assertFalse(sessionManager.isSessionExpired())
    }

    @Test
    fun `10분 이내 활동 → isSessionExpired() = false`() {
        // 현재 시각 기준 5분 전 활동
        storage["last_active_ms"] = System.currentTimeMillis() - (5 * 60 * 1000L)
        assertFalse(sessionManager.isSessionExpired())
    }

    @Test
    fun `10분 초과 → isSessionExpired() = true`() {
        // 현재 시각 기준 11분 전 활동
        storage["last_active_ms"] = System.currentTimeMillis() - (11 * 60 * 1000L)
        assertTrue(sessionManager.isSessionExpired())
    }
}
