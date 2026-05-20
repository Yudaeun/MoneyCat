package com.moneycat.domain.usecase

import com.moneycat.domain.model.AiInsight
import com.moneycat.domain.model.BenefitType
import com.moneycat.domain.model.InsightType
import com.moneycat.domain.model.TransactionType
import com.moneycat.domain.repository.AiInsightRepository
import com.moneycat.domain.repository.BudgetRepository
import com.moneycat.domain.repository.CardRepository
import com.moneycat.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.first
import java.math.BigDecimal
import java.math.MathContext
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.YearMonth
import java.util.Locale
import javax.inject.Inject

class GenerateMonthlyInsightUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val budgetRepository: BudgetRepository,
    private val aiInsightRepository: AiInsightRepository,
    private val cardRepository: CardRepository,
) {
    suspend operator fun invoke(yearMonth: YearMonth) {
        val start = yearMonth.atDay(1)
        val end = yearMonth.atEndOfMonth()
        val transactions = transactionRepository.getByDateRange(start, end).first()
        val budgets = budgetRepository.getByMonth(yearMonth.toString()).first()
        val expenses = transactions.filter { it.type == TransactionType.EXPENSE }
        val now = LocalDateTime.now()

        if (expenses.isEmpty()) return

        val totalExpense = expenses.fold(BigDecimal.ZERO) { acc, t -> acc + t.amount }
        val totalBudget = budgets.fold(BigDecimal.ZERO) { acc, b -> acc + b.limitAmount }

        // 예산 초과 알림
        if (totalBudget > BigDecimal.ZERO) {
            val ratio = totalExpense.toFloat() / totalBudget.toFloat()
            if (ratio >= 0.9f) {
                aiInsightRepository.insert(
                    AiInsight(
                        type = InsightType.SPENDING_ALERT,
                        title = "예산 한도에 근접했어요",
                        content = "이번 달 지출이 예산의 ${(ratio * 100).toInt()}%에 도달했습니다. 남은 기간 지출을 줄여보세요.",
                        createdAt = now,
                    )
                )
            }
        }

        // 최다 지출 카테고리 절약 팁
        val topCategory = expenses
            .groupBy { it.category }
            .mapValues { (_, txs) -> txs.fold(BigDecimal.ZERO) { acc, t -> acc + t.amount } }
            .maxByOrNull { it.value }
        topCategory?.let { (cat, amount) ->
            val saving = amount.multiply(BigDecimal("0.1"))
            aiInsightRepository.insert(
                AiInsight(
                    type = InsightType.SAVING_TIP,
                    title = "$cat 지출, 10% 줄여볼까요?",
                    content = "이번 달 $cat 지출이 ${formatWon(amount)}로 가장 많아요. 10%만 아껴도 ${formatWon(saving)}을 절약할 수 있어요.",
                    estimatedSaving = saving,
                    createdAt = now,
                )
            )
        }

        // 카드 혜택 제안 (CARD_SUGGESTION)
        val topCategoryName = topCategory?.key
        if (topCategoryName != null && expenses.size >= 3) {
            val activeCards = cardRepository.getActiveCards().first()
            val cardBenefitPairs = activeCards.flatMap { card ->
                val withBenefits = cardRepository.getCardWithBenefits(card.id).first()
                withBenefits?.benefits?.map { benefit -> card to benefit } ?: emptyList()
            }
            val matched = cardBenefitPairs.firstOrNull { pair ->
                pair.second.category.contains(topCategoryName, ignoreCase = true) ||
                    topCategoryName.contains(pair.second.category, ignoreCase = true)
            }
            if (matched != null) {
                val card = matched.first
                val benefit = matched.second
                val benefitDesc = when (benefit.benefitType) {
                    BenefitType.DISCOUNT ->
                        "${benefit.discountRate?.multiply(BigDecimal("100"))?.toInt() ?: 0}% 할인"
                    BenefitType.CASHBACK -> "캐시백"
                    BenefitType.POINT -> "포인트 적립"
                }
                aiInsightRepository.insert(
                    AiInsight(
                        type = InsightType.CARD_SUGGESTION,
                        title = "${card.name}으로 절약해 보세요",
                        content = "이번 달 가장 많이 쓴 '$topCategoryName' 카테고리에서 " +
                            "${card.bankName} ${card.name}의 $benefitDesc 혜택을 활용할 수 있어요. " +
                            benefit.description,
                        createdAt = now,
                    )
                )
            }
        }

        // 이상 지출 감지
        if (expenses.size >= 3) {
            val avg = totalExpense.divide(BigDecimal(expenses.size), MathContext.DECIMAL32)
            val anomaly = expenses.maxByOrNull { it.amount }
            if (anomaly != null && anomaly.amount > avg.multiply(BigDecimal("2.5"))) {
                aiInsightRepository.insert(
                    AiInsight(
                        type = InsightType.ANOMALY,
                        title = "평소보다 큰 지출이 감지됐어요",
                        content = "${anomaly.date}에 ${anomaly.category}에서 ${formatWon(anomaly.amount)}의 지출이 있었습니다. 평균의 ${(anomaly.amount.toFloat() / avg.toFloat()).toInt()}배예요.",
                        createdAt = now,
                    )
                )
            }
        }

        // 월간 요약
        val incomeTotal = transactions
            .filter { it.type == TransactionType.INCOME }
            .fold(BigDecimal.ZERO) { acc, t -> acc + t.amount }
        aiInsightRepository.insert(
            AiInsight(
                type = InsightType.WEEKLY_SUMMARY,
                title = "${yearMonth.monthValue}월 소비 리포트",
                content = "수입 ${formatWon(incomeTotal)}, 지출 ${formatWon(totalExpense)}\n총 ${expenses.size}건의 지출이 기록됐습니다.",
                createdAt = now,
            )
        )
    }

    private fun formatWon(amount: BigDecimal): String {
        val nf = NumberFormat.getNumberInstance(Locale.KOREA)
        return "₩${nf.format(amount)}"
    }
}
