package com.day.moneycat.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.moneycat.domain.repository.BudgetRepository
import com.moneycat.domain.repository.TransactionRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.math.BigDecimal
import java.time.YearMonth

@HiltWorker
class BudgetAlertWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val budgetRepository: BudgetRepository,
    private val transactionRepository: TransactionRepository,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val ym = YearMonth.now()
        val budgets = budgetRepository.getByMonth(ym.toString()).first()
        if (budgets.isEmpty()) return Result.success()

        val categoryTotals = transactionRepository
            .getMonthlyCategoryTotals(ym.atDay(1), ym.atEndOfMonth())
            .first()
        val spendingMap = categoryTotals.associate { it.category to it.total }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        ensureChannel(notificationManager)

        budgets.forEach { budget ->
            val spent = spendingMap[budget.category] ?: BigDecimal.ZERO
            if (budget.limitAmount <= BigDecimal.ZERO) return@forEach
            val ratio = spent.toFloat() / budget.limitAmount.toFloat()
            if (ratio < budget.alertThreshold) return@forEach

            val title = if (ratio >= 1f) "⚠️ 예산 초과" else "💸 예산 주의"
            val msg = "${budget.category} 예산 ${(ratio * 100).toInt()}% 사용 중" +
                    " (${formatWon(spent)} / ${formatWon(budget.limitAmount)})"

            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(msg)
                .setStyle(NotificationCompat.BigTextStyle().bigText(msg))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build()

            notificationManager.notify(budget.category.hashCode(), notification)
        }
        return Result.success()
    }

    private fun ensureChannel(manager: NotificationManager) {
        if (manager.getNotificationChannel(CHANNEL_ID) != null) return
        val channel = NotificationChannel(
            CHANNEL_ID, "예산 알림", NotificationManager.IMPORTANCE_DEFAULT
        ).apply { description = "카테고리 예산 초과·임박 알림" }
        manager.createNotificationChannel(channel)
    }

    private fun formatWon(amount: BigDecimal): String {
        return "₩${"%,.0f".format(amount)}"
    }

    companion object {
        const val CHANNEL_ID = "moneycat_budget_alert"
        const val WORK_NAME = "BudgetAlertWork"
    }
}
