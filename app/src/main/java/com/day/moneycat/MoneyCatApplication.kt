package com.day.moneycat

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.hilt.work.HiltWorkerFactory
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.day.moneycat.worker.BudgetAlertWorker
import com.moneycat.security.RootDetector
import com.moneycat.security.SessionManager
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class MoneyCatApplication : Application(), Configuration.Provider {

    @Inject lateinit var sessionManager: SessionManager
    @Inject lateinit var rootDetector: RootDetector
    @Inject lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        scheduleBudgetAlert()
        registerLifecycleObserver()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            BudgetAlertWorker.CHANNEL_ID,
            "예산 알림",
            NotificationManager.IMPORTANCE_DEFAULT,
        ).apply { description = "카테고리 예산 초과·임박 알림" }
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    private fun scheduleBudgetAlert() {
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            BudgetAlertWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            PeriodicWorkRequestBuilder<BudgetAlertWorker>(12, TimeUnit.HOURS).build(),
        )
    }

    private fun registerLifecycleObserver() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(
            object : DefaultLifecycleObserver {
                override fun onStop(owner: LifecycleOwner) {
                    sessionManager.updateLastActive()
                }
            }
        )
    }
}
