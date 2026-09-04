package com.devdd.mypayments.data

import androidx.compose.runtime.Immutable

@Immutable
data class ServicePreset(
    val name: String,
    val category: String,
    val defaultAmount: Double,
    val currency: String = "₽",
    val iconName: String,
    val colorHex: String,
    val websiteUrl: String? = null,
    val defaultType: ExpenseType = ExpenseType.SUBSCRIPTION
)

object Presets {
    val creatorsPresets = listOf(
        ServicePreset("Boosty", "Creators", 300.0, "₽", "star", "#F15A24", "https://boosty.to"),
        ServicePreset("Patreon", "Creators", 500.0, "₽", "star", "#FF424D", "https://patreon.com"),
        ServicePreset("VK Donut", "Creators", 150.0, "₽", "send", "#0077FF", "https://vk.com"),
        ServicePreset("Sponsr", "Creators", 250.0, "₽", "star", "#10B981", "https://sponsr.ru"),
        ServicePreset("Twitch Sub", "Creators", 399.0, "₽", "headset", "#9146FF", "https://twitch.tv"),
        ServicePreset("YouTube Membership", "Creators", 299.0, "₽", "play_circle", "#FF0000", "https://youtube.com")
    )

    val socialPresets = listOf(
        ServicePreset("Telegram Premium", "Social", 299.0, "₽", "send", "#229ED9", "https://t.me"),
        ServicePreset("Discord Nitro", "Social", 499.0, "₽", "headset", "#5865F2", "https://discord.com"),
        ServicePreset("VK Combo / Музыка", "Social", 169.0, "₽", "music_note", "#0077FF", "https://vk.com/vk_music")
    )

    val aiPresets = listOf(
        ServicePreset("ChatGPT Plus", "AI", 1990.0, "₽", "smart_toy", "#10A37F", "https://chatgpt.com"),
        ServicePreset("Claude Pro", "AI", 1990.0, "₽", "psychology", "#D97706", "https://claude.ai"),
        ServicePreset("Midjourney", "AI", 990.0, "₽", "palette", "#2563EB", "https://midjourney.com"),
        ServicePreset("GitHub Copilot", "AI", 990.0, "₽", "code", "#181717", "https://github.com"),
        ServicePreset("Gemini Advanced", "AI", 1990.0, "₽", "auto_awesome", "#8E24AA", "https://gemini.google.com"),
        ServicePreset("Perplexity Pro", "AI", 1990.0, "₽", "smart_toy", "#059669", "https://perplexity.ai")
    )

    val musicPresets = listOf(
        ServicePreset("Яндекс Плюс / Музыка", "Music", 299.0, "₽", "music_note", "#FFCC00", "https://plus.yandex.ru"),
        ServicePreset("Spotify", "Music", 199.0, "₽", "graphic_eq", "#1DB954", "https://spotify.com/account"),
        ServicePreset("Apple Music", "Music", 169.0, "₽", "headset", "#FA243C", "https://music.apple.com"),
        ServicePreset("YouTube Music", "Music", 299.0, "₽", "play_circle", "#FF0000", "https://youtube.com/paid_memberships"),
        ServicePreset("VK Music", "Music", 169.0, "₽", "queue_music", "#0077FF", "https://vk.com/vk_music"),
        ServicePreset("Звук (СберМузыка)", "Music", 199.0, "₽", "headset", "#22C55E", "https://zvuk.com")
    )

    val moviePresets = listOf(
        ServicePreset("Кинопоиск HD", "Movies", 299.0, "₽", "movie", "#FF5500", "https://hd.kinopoisk.ru"),
        ServicePreset("Иви (Ivi)", "Movies", 399.0, "₽", "live_tv", "#EA5388", "https://ivi.ru"),
        ServicePreset("Okko", "Movies", 199.0, "₽", "theaters", "#812082", "https://okko.tv"),
        ServicePreset("Start", "Movies", 399.0, "₽", "video_library", "#FF0055", "https://start.ru"),
        ServicePreset("Premier", "Movies", 299.0, "₽", "movie", "#0284C7", "https://premier.one"),
        ServicePreset("Wink", "Movies", 399.0, "₽", "tv", "#FF6B00", "https://wink.ru"),
        ServicePreset("KION", "Movies", 249.0, "₽", "live_tv", "#E11D48", "https://kion.ru"),
        ServicePreset("Netflix", "Movies", 1290.0, "₽", "tv", "#E50914", "https://netflix.com/youraccount")
    )

    val cloudPresets = listOf(
        ServicePreset("iCloud+", "Cloud", 99.0, "₽", "cloud", "#007AFF", "https://appleid.apple.com"),
        ServicePreset("Google One", "Cloud", 199.0, "₽", "cloud_queue", "#4285F4", "https://one.google.com"),
        ServicePreset("Яндекс 360", "Cloud", 149.0, "₽", "cloud", "#FC3F1D", "https://360.yandex.ru"),
        ServicePreset("VPN Service", "Cloud", 299.0, "₽", "security", "#7C3AED", "https://t.me")
    )

    val gamePresets = listOf(
        ServicePreset("Steam", "Games", 1500.0, "₽", "sports_esports", "#171A21", "https://store.steampowered.com", ExpenseType.GAME_LIMIT),
        ServicePreset("PlayStation Network", "Games", 3000.0, "₽", "gamepad", "#003791", "https://store.playstation.com", ExpenseType.GAME_LIMIT),
        ServicePreset("Xbox Game Pass", "Games", 2500.0, "₽", "sports_esports", "#107C41", "https://xbox.com", ExpenseType.GAME_LIMIT),
        ServicePreset("Epic Games Store", "Games", 1000.0, "₽", "extension", "#313131", "https://store.epicgames.com", ExpenseType.GAME_LIMIT),
        ServicePreset("VK Play", "Games", 1000.0, "₽", "sports_esports", "#0077FF", "https://vkplay.ru", ExpenseType.GAME_LIMIT),
        ServicePreset("Genshin Impact", "Games", 1000.0, "₽", "auto_awesome", "#3B82F6", "https://genshin.hoyoverse.com", ExpenseType.GAME_LIMIT),
        ServicePreset("Honkai: Star Rail", "Games", 1000.0, "₽", "star", "#8B5CF6", "https://hsr.hoyoverse.com", ExpenseType.GAME_LIMIT),
        ServicePreset("Brawl Stars", "Games", 500.0, "₽", "bolt", "#F59E0B", "https://supercell.com", ExpenseType.GAME_LIMIT),
        ServicePreset("Roblox", "Games", 500.0, "₽", "extension", "#EC4899", "https://roblox.com", ExpenseType.GAME_LIMIT),
        ServicePreset("CS2 / Dota 2", "Games", 1000.0, "₽", "shield", "#EF4444", "https://store.steampowered.com", ExpenseType.GAME_LIMIT),
        ServicePreset("World of Tanks (Lesta)", "Games", 1000.0, "₽", "shield", "#D97706", "https://lesta.ru", ExpenseType.GAME_LIMIT)
    )

    val utilityPresets = listOf(
        ServicePreset("ЖКХ (Коммунальные услуги)", "Utilities", 5000.0, "₽", "home", "#0284C7", null, ExpenseType.MONTHLY_BILL),
        ServicePreset("Электричество", "Utilities", 1200.0, "₽", "bolt", "#EAB308", null, ExpenseType.MONTHLY_BILL),
        ServicePreset("Водоснабжение", "Utilities", 1000.0, "₽", "water_drop", "#06B6D4", null, ExpenseType.MONTHLY_BILL),
        ServicePreset("Газ", "Utilities", 500.0, "₽", "bolt", "#F97316", null, ExpenseType.MONTHLY_BILL),
        ServicePreset("Интернет и Домашнее ТВ", "Utilities", 650.0, "₽", "wifi", "#3B82F6", null, ExpenseType.MONTHLY_BILL),
        ServicePreset("МТС (Мобильная связь)", "Utilities", 500.0, "₽", "smartphone", "#E30613", "https://mts.ru", ExpenseType.MONTHLY_BILL),
        ServicePreset("Билайн (Мобильная связь)", "Utilities", 500.0, "₽", "smartphone", "#FFCC00", "https://beeline.ru", ExpenseType.MONTHLY_BILL),
        ServicePreset("МегаФон (Мобильная связь)", "Utilities", 500.0, "₽", "smartphone", "#00B956", "https://megafon.ru", ExpenseType.MONTHLY_BILL),
        ServicePreset("t2 / Tele2 (Мобильная связь)", "Utilities", 500.0, "₽", "smartphone", "#1F2937", "https://t2.ru", ExpenseType.MONTHLY_BILL),
        ServicePreset("Аренда жилья", "Utilities", 25000.0, "₽", "apartment", "#8B5CF6", null, ExpenseType.MONTHLY_BILL)
    )
}