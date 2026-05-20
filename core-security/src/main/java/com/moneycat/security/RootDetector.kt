package com.moneycat.security

import android.os.Build
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RootDetector @Inject constructor() {

    private val suPaths = listOf(
        "/system/bin/su", "/system/xbin/su", "/sbin/su",
        "/system/su", "/system/bin/.ext/su", "/system/usr/we-need-root/su-backup",
        "/data/local/xbin/su", "/data/local/bin/su", "/data/local/su"
    )

    private val rootPackages = listOf(
        "com.noshufou.android.su",
        "com.noshufou.android.su.elite",
        "eu.chainfire.supersu",
        "com.koushikdutta.superuser",
        "com.topjohnwu.magisk",
        "io.github.vvb2060.magisk",
        "com.kingroot.kinguser"
    )

    fun isRooted(): Boolean = checkSuBinaries() || checkRootPackages() || checkBuildTags()

    private fun checkSuBinaries(): Boolean = suPaths.any { File(it).exists() }

    private fun checkRootPackages(): Boolean {
        // 설치된 패키지 목록에서 루팅 앱 확인
        return try {
            val process = Runtime.getRuntime().exec(arrayOf("pm", "list", "packages"))
            val output = process.inputStream.bufferedReader().readText()
            rootPackages.any { pkg -> output.contains(pkg) }
        } catch (_: Exception) {
            false
        }
    }

    private fun checkBuildTags(): Boolean {
        return Build.TAGS?.contains("test-keys") == true
    }
}
