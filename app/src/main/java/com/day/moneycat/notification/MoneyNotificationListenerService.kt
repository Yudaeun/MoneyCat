package com.day.moneycat.notification

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.moneycat.data.local.db.dao.NotificationRuleDao
import com.moneycat.domain.model.InputSource
import com.moneycat.domain.model.PaymentMethod
import com.moneycat.domain.model.Transaction
import com.moneycat.domain.model.TransactionType
import com.moneycat.domain.usecase.AddTransactionUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class MoneyNotificationListenerService : NotificationListenerService() {

    @Inject lateinit var notificationRuleDao: NotificationRuleDao
    @Inject lateinit var addTransactionUseCase: AddTransactionUseCase

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onListenerConnected() {
        scope.launch {
            val existing = notificationRuleDao.getEnabled()
            if (existing.isEmpty()) {
                notificationRuleDao.insertAll(DefaultNotificationRules.rules)
            }
        }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        scope.launch {
            val rule = notificationRuleDao.findByPackage(sbn.packageName) ?: return@launch
            if (!rule.isEnabled) return@launch

            val extras = sbn.notification.extras
            val title = extras.getString(Notification.EXTRA_TITLE) ?: return@launch
            val body = extras.getString(Notification.EXTRA_TEXT) ?: return@launch

            val titleMatches = rule.titlePattern.isEmpty() || title.contains(rule.titlePattern)
            if (!titleMatches) return@launch

            val match = Regex(rule.bodyPattern).find(body) ?: return@launch
            val amountStr = match.groupValues.getOrNull(rule.amountGroup)
                ?.replace(",", "") ?: return@launch
            val amount = amountStr.toBigDecimalOrNull() ?: return@launch
            if (amount <= java.math.BigDecimal.ZERO) return@launch

            val merchant = rule.merchantGroup
                ?.let { match.groupValues.getOrNull(it)?.trim() }
                ?.takeIf { it.isNotBlank() }
                ?: rule.bankName

            addTransactionUseCase(
                Transaction(
                    type = TransactionType.EXPENSE,
                    amount = amount,
                    category = "기타",
                    description = merchant,
                    date = LocalDate.now(),
                    paymentMethod = PaymentMethod.CARD,
                    source = InputSource.NOTIFICATION,
                    createdAt = LocalDateTime.now(),
                )
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
