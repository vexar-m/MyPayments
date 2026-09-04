package com.devdd.mypayments.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

object IconPicker {

    val availableIcons = mapOf(
        "smart_toy" to Icons.Default.SmartToy,
        "music_note" to Icons.Default.MusicNote,
        "movie" to Icons.Default.Movie,
        "tv" to Icons.Default.Tv,
        "home" to Icons.Default.Home,
        "sports_esports" to Icons.Default.SportsEsports,
        "gamepad" to Icons.Default.Gamepad,
        "cloud" to Icons.Default.Cloud,
        "wifi" to Icons.Default.Wifi,
        "bolt" to Icons.Default.Bolt,
        "water_drop" to Icons.Default.WaterDrop,
        "smartphone" to Icons.Default.Smartphone,
        "apartment" to Icons.Default.Apartment,
        "palette" to Icons.Default.Palette,
        "code" to Icons.Default.Code,
        "psychology" to Icons.Default.Psychology,
        "auto_awesome" to Icons.Default.AutoAwesome,
        "headset" to Icons.Default.Headset,
        "send" to Icons.Default.Send,
        "security" to Icons.Default.Security,
        "subscriptions" to Icons.Default.Subscriptions,
        "extension" to Icons.Default.Extension,
        "shield" to Icons.Default.Shield,
        "star" to Icons.Default.Star
    )

    fun getIcon(name: String): ImageVector {
        return availableIcons[name] ?: Icons.Default.Star
    }
}

@Composable
fun PaymentIcon(
    iconName: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified
) {
    Icon(
        imageVector = IconPicker.getIcon(iconName),
        contentDescription = contentDescription,
        modifier = modifier,
        tint = tint
    )
}