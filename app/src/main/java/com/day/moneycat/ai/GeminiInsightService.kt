package com.day.moneycat.ai

import com.day.moneycat.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiInsightService @Inject constructor(
    private val model: GenerativeModel,
) {
    suspend fun analyze(prompt: String): Result<String> {
        if (BuildConfig.GEMINI_API_KEY.isEmpty()) {
            return Result.failure(IllegalStateException("GEMINI_API_KEY not set in local.properties"))
        }
        return runCatching {
            val response = model.generateContent(prompt)
            response.text ?: "분석 결과를 가져올 수 없습니다."
        }
    }
}
