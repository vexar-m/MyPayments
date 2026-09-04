package com.devdd.mypayments.data

import androidx.compose.runtime.Immutable

@Immutable
data class AppSettings(
    val language: String = "ru", // "ru" or "en"
    val themeMode: String = "SYSTEM", // "SYSTEM", "LIGHT", "DARK"
    val accentColor: String = "WALLPAPER", // "WALLPAPER", "PURPLE", "BLUE", "EMERALD", "CORAL", "GOLD"
    val notificationsEnabled: Boolean = true,
    val isFirstLaunch: Boolean = true,
    val monthlyBudgetLimit: Double? = null // Maximum budget limit for subscriptions per month
)