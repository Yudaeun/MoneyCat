package com.moneycat.security

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PinAuthManagerTest {

    private lateinit var cryptoManager: CryptoManager
    private lateinit var pinAuthManager: PinAuthManager
    private val storage = mutableMapOf<String, String>()

    @Before
    fun setUp() {
        cryptoManager = mockk()
        val keySlot = slot<String>()
        val valueSlot = slot<String>()

        every { cryptoManager.contains(any()) } answers { storage.containsKey(firstArg()) }
        every { cryptoManager.getEncrypted(capture(keySlot)) } answers { storage[keySlot.captured] }
        every { cryptoManager.saveEncrypted(capture(keySlot), capture(valueSlot)) } answers {
            storage[keySlot.captured] = valueSlot.captured
        }
        every { cryptoManager.removeEncrypted(any()) } answers { storage.remove(firstArg()); Unit }
        every { cryptoManager.generateSalt() } returns "dGVzdHNhbHQxMjM0NTY3OA=="
        every { cryptoManager.hashPin(any(), any()) } answers {
            // 테스트에서는 단순 해시 시뮬레이션
            "${firstArg<String>()}_hashed_${secondArg<String>()}"
        }

        pinAuthManager = PinAuthManager(cryptoManager)
    }

    @Test
    fun `PIN 미설정 상태에서 verifyPin → NotSet 반환`() {
        val result = pinAuthManager.verifyPin("123456")
        assertEquals(PinResult.NotSet, result)
    }

    @Test
    fun `PIN 설정 후 올바른 PIN 입력 → Success 반환`() {
        pinAuthManager.setupPin("123456")
        val result = pinAuthManager.verifyPin("123456")
        assertEquals(PinResult.Success, result)
    }

    @Test
    fun `잘못된 PIN 1회 입력 → Wrong(remaining=9) 반환`() {
        pinAuthManager.setupPin("123456")
        val result = pinAuthManager.verifyPin("000000")
        assertTrue(result is PinResult.Wrong)
        assertEquals(9, (result as PinResult.Wrong).remaining)
    }

    @Test
    fun `10회 실패 → Locked 반환`() {
        pinAuthManager.setupPin("123456")
        repeat(9) { pinAuthManager.verifyPin("000000") }
        val result = pinAuthManager.verifyPin("000000")
        assertTrue(result is PinResult.Locked)
    }

    @Test
    fun `성공 후 실패 카운트 리셋 → 다시 9회 남음`() {
        pinAuthManager.setupPin("123456")
        repeat(5) { pinAuthManager.verifyPin("000000") }
        pinAuthManager.verifyPin("123456") // 성공 → 리셋
        val result = pinAuthManager.verifyPin("000000")
        assertTrue(result is PinResult.Wrong)
        assertEquals(9, (result as PinResult.Wrong).remaining)
    }
}
