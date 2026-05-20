package com.moneycat.ui.theme

import androidx.compose.ui.graphics.Color

// MoneyCat 브랜드 팔레트
val MintSage = Color(0xFFC4E2E2)        // Primary: 민트 세이지
val GrayMint = Color(0xFFB5C3C3)        // Muted: 그레이 민트
val CatEye = Color(0xFF9DD8E0)          // Cat Eye: 연하늘
val CatBody = Color(0xFF2A2A2A)         // Cat Body: 검은 고양이
val BgLight = Color(0xFFE8F4F4)         // Background Light
val BgDark = Color(0xFF1E2A2A)          // Background Dark
val IncomeGreen = Color(0xFF5AB0A0)     // 수입
val ExpenseRed = Color(0xFFE07070)      // 지출
val WarningPink = Color(0xFFE8A0AA)     // Warning: 고양이 코 색
val CatBodyLight = Color(0xFF333333)    // Cat Body 밝은 버전

// Light Theme
val md_theme_light_primary = MintSage
val md_theme_light_onPrimary = CatBody
val md_theme_light_primaryContainer = Color(0xFFD6EEEE)
val md_theme_light_onPrimaryContainer = Color(0xFF002020)
val md_theme_light_secondary = GrayMint
val md_theme_light_onSecondary = CatBody
val md_theme_light_background = BgLight
val md_theme_light_onBackground = CatBody
val md_theme_light_surface = Color(0xFFFAFDFD)
val md_theme_light_onSurface = CatBody
val md_theme_light_error = ExpenseRed
val md_theme_light_onError = Color.White

// Dark Theme
val md_theme_dark_primary = CatEye
val md_theme_dark_onPrimary = Color(0xFF003737)
val md_theme_dark_primaryContainer = Color(0xFF004F4F)
val md_theme_dark_onPrimaryContainer = MintSage
val md_theme_dark_secondary = GrayMint
val md_theme_dark_onSecondary = Color(0xFF273333)
val md_theme_dark_background = BgDark
val md_theme_dark_onBackground = Color(0xFFD8E6E6)
val md_theme_dark_surface = Color(0xFF1E2A2A)
val md_theme_dark_onSurface = Color(0xFFD8E6E6)
val md_theme_dark_error = Color(0xFFFFB4AB)
val md_theme_dark_onError = Color(0xFF690005)
