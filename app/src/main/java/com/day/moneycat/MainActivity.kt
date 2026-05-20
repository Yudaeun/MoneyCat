package com.day.moneycat

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.day.moneycat.auth.AuthScreen
import com.day.moneycat.main.MainScreen
import com.day.moneycat.main.StartupViewModel
import com.day.moneycat.ocr.OcrScanScreen
import com.day.moneycat.onboarding.OnboardingScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.day.moneycat.transaction.AddTransactionScreen
import com.moneycat.security.BiometricAuthManager
import com.moneycat.ui.theme.MoneyCatTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.navigation.compose.hiltViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject lateinit var biometricAuthManager: BiometricAuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoneyCatTheme {
                val startupVm: StartupViewModel = hiltViewModel()
                val isOnboardingCompleted by startupVm.isOnboardingCompleted.collectAsState()
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "auth") {
                    composable("auth") {
                        AuthScreen(
                            onAuthenticated = {
                                val dest = if (isOnboardingCompleted == true) "main" else "onboarding"
                                navController.navigate(dest) {
                                    popUpTo("auth") { inclusive = true }
                                }
                            },
                            biometricAuthManager = biometricAuthManager,
                        )
                    }
                    composable("onboarding") {
                        OnboardingScreen(
                            onComplete = {
                                navController.navigate("main") {
                                    popUpTo("onboarding") { inclusive = true }
                                }
                            }
                        )
                    }
                    composable("main") {
                        MainScreen(
                            onAddTransaction = { navController.navigate("add_transaction") },
                            onEditTransaction = { id -> navController.navigate("edit_transaction/$id") },
                        )
                    }
                    composable("add_transaction") { backStackEntry ->
                        val ocrAmount = backStackEntry.savedStateHandle.get<String>("ocr_amount") ?: ""
                        AddTransactionScreen(
                            onBack = { navController.popBackStack() },
                            onScanReceipt = { navController.navigate("scan_receipt") },
                            ocrAmount = ocrAmount,
                        )
                    }
                    composable(
                        route = "edit_transaction/{id}",
                        arguments = listOf(navArgument("id") { type = NavType.LongType }),
                    ) {
                        AddTransactionScreen(
                            onBack = { navController.popBackStack() },
                        )
                    }
                    composable("scan_receipt") {
                        OcrScanScreen(
                            onAmountDetected = { amount ->
                                navController.previousBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("ocr_amount", amount)
                                navController.popBackStack()
                            },
                            onBack = { navController.popBackStack() },
                        )
                    }
                }
            }
        }
    }
}
