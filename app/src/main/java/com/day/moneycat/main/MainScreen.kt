package com.day.moneycat.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.day.moneycat.asset.AssetScreen
import com.day.moneycat.budget.BudgetScreen
import com.day.moneycat.card.CardListScreen
import com.day.moneycat.home.HomeScreen
import com.day.moneycat.insight.AiInsightScreen
import com.day.moneycat.settings.ProfileEditScreen
import com.day.moneycat.settings.SettingsScreen
import com.day.moneycat.statistics.StatisticsScreen
import com.day.moneycat.transaction.TransactionListScreen

private data class TabItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

private val tabs = listOf(
    TabItem("home", "홈", Icons.Filled.Home, Icons.Outlined.Home),
    TabItem("transactions", "거래", Icons.AutoMirrored.Filled.ReceiptLong, Icons.AutoMirrored.Outlined.ReceiptLong),
    TabItem("insights", "AI", Icons.Filled.AutoAwesome, Icons.Outlined.AutoAwesome),
    TabItem("statistics", "통계", Icons.Filled.BarChart, Icons.Outlined.BarChart),
    TabItem("settings", "설정", Icons.Filled.Settings, Icons.Outlined.Settings),
)

@Composable
fun MainScreen(
    onAddTransaction: () -> Unit,
    onEditTransaction: (Long) -> Unit = {},
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = navBackStackEntry?.destination?.route

    // budget/asset 화면에서는 FAB 숨김 (각 화면이 자체 FAB 보유)
    val showFab = currentRoute in tabs.map { it.route }

    Scaffold(
        bottomBar = {
            // budget/asset 서브화면에서는 하단바 숨김
            if (currentRoute == null || tabs.any { it.route == currentRoute }) {
                NavigationBar {
                    tabs.forEach { tab ->
                        val selected = currentDestination?.hierarchy?.any { it.route == tab.route } == true
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    if (selected) tab.selectedIcon else tab.unselectedIcon,
                                    contentDescription = tab.label,
                                )
                            },
                            label = { Text(tab.label) },
                            selected = selected,
                            onClick = {
                                navController.navigate(tab.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            if (showFab) {
                FloatingActionButton(
                    onClick = onAddTransaction,
                    containerColor = MaterialTheme.colorScheme.primary,
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "거래 추가")
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = tabs[0].route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(tabs[0].route) {
                HomeScreen(
                    onNavigateToAsset = { navController.navigate("asset") },
                    onNavigateToInsights = { navController.navigate("insights") },
                )
            }
            composable(tabs[1].route) {
                TransactionListScreen(onEditTransaction = onEditTransaction)
            }
            composable(tabs[2].route) { AiInsightScreen() }
            composable(tabs[3].route) {
                StatisticsScreen(onNavigateToBudget = { navController.navigate("budget") })
            }
            composable(tabs[4].route) {
                SettingsScreen(
                    onNavigateToBudget = { navController.navigate("budget") },
                    onNavigateToAsset = { navController.navigate("asset") },
                    onNavigateToProfileEdit = { navController.navigate("profile_edit") },
                    onNavigateToCardList = { navController.navigate("card_list") },
                )
            }
            composable("budget") { BudgetScreen(onBack = { navController.popBackStack() }) }
            composable("asset") { AssetScreen(onBack = { navController.popBackStack() }) }
            composable("profile_edit") { ProfileEditScreen(onBack = { navController.popBackStack() }) }
            composable("card_list") { CardListScreen(onBack = { navController.popBackStack() }) }
        }
    }
}
