package com.devdd.mypayments.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val PurpleLightScheme = lightColorScheme(
    primary = PurplePrimaryLight,
    secondary = PurpleSecondaryLight,
    tertiary = PurpleTertiaryLight
)

private val PurpleDarkScheme = darkColorScheme(
    primary = PurplePrimaryDark,
    secondary = PurpleSecondaryDark,
    tertiary = PurpleTertiaryDark
)

private val BlueLightScheme = lightColorScheme(
    primary = BluePrimaryLight,
    secondary = BluePrimaryLight.copy(alpha = 0.7f),
    tertiary = PurpleTertiaryLight
)

private val BlueDarkScheme = darkColorScheme(
    primary = BluePrimaryDark,
    secondary = BluePrimaryDark.copy(alpha = 0.7f),
    tertiary = PurpleTertiaryDark
)

private val EmeraldLightScheme = lightColorScheme(
    primary = EmeraldPrimaryLight,
    secondary = EmeraldPrimaryLight.copy(alpha = 0.7f),
    tertiary = PurpleTertiaryLight
)

private val EmeraldDarkScheme = darkColorScheme(
    primary = EmeraldPrimaryDark,
    secondary = EmeraldPrimaryDark.copy(alpha = 0.7f),
    tertiary = PurpleTertiaryDark
)

private val CoralLightScheme = lightColorScheme(
    primary = CoralPrimaryLight,
    secondary = CoralPrimaryLight.copy(alpha = 0.7f),
    tertiary = PurpleTertiaryLight
)

private val CoralDarkScheme = darkColorScheme(
    primary = CoralPrimaryDark,
    secondary = CoralPrimaryDark.copy(alpha = 0.7f),
    tertiary = PurpleTertiaryDark
)

private val GoldLightScheme = lightColorScheme(
    primary = GoldPrimaryLight,
    secondary = GoldPrimaryLight.copy(alpha = 0.7f),
    tertiary = PurpleTertiaryLight
)

private val GoldDarkScheme = darkColorScheme(
    primary = GoldPrimaryDark,
    secondary = GoldPrimaryDark.copy(alpha = 0.7f),
    tertiary = PurpleTertiaryDark
)

@Composable
fun MyPaymentsTheme(
    themeMode: String = "SYSTEM", // "SYSTEM", "LIGHT", "DARK"
    accentColor: String = "WALLPAPER", // "WALLPAPER", "PURPLE", "BLUE", "EMERALD", "CORAL", "GOLD"
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        "LIGHT" -> false
        "DARK" -> true
        else -> isSystemInDarkTheme()
    }

    val context = LocalContext.current

    val colorScheme: ColorScheme = when {
        accentColor == "WALLPAPER" && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        accentColor == "BLUE" -> if (darkTheme) BlueDarkScheme else BlueLightScheme
        accentColor == "EMERALD" -> if (darkTheme) EmeraldDarkScheme else EmeraldLightScheme
        accentColor == "CORAL" -> if (darkTheme) CoralDarkScheme else CoralLightScheme
        accentColor == "GOLD" -> if (darkTheme) GoldDarkScheme else GoldLightScheme
        else -> if (darkTheme) PurpleDarkScheme else PurpleLightScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}