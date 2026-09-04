package com.devdd.mypayments.data

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject

class PaymentRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("my_payments_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_PAYMENTS = "key_payments_v3"
        private const val KEY_LANG = "key_language"
        private const val KEY_THEME = "key_theme"
        private const val KEY_ACCENT = "key_accent"
        private const val KEY_NOTIF = "key_notifications"
        private const val KEY_FIRST_LAUNCH = "key_first_launch"
        private const val KEY_BUDGET_LIMIT = "key_budget_limit"
    }

    fun getPayments(): List<PaymentItem> {
        val jsonString = prefs.getString(KEY_PAYMENTS, null) ?: return emptyList()
        return try {
            val array = JSONArray(jsonString)
            val list = mutableListOf<PaymentItem>()
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    PaymentItem(
                        id = obj.optString("id"),
                        name = obj.optString("name"),
                        type = ExpenseType.valueOf(obj.optString("type", ExpenseType.SUBSCRIPTION.name)),
                        category = obj.optString("category", "Other"),
                        amount = obj.optDouble("amount", 0.0),
                        currency = obj.optString("currency", "₽"),
                        startDate = obj.optLong("startDate", System.currentTimeMillis()),
                        endDate = if (obj.has("endDate") && !obj.isNull("endDate")) obj.optLong("endDate") else null,
                        paymentDayOfMonth = obj.optInt("paymentDayOfMonth", 1),
                        paymentTime = obj.optString("paymentTime", "10:00"),
                        iconName = obj.optString("iconName", "star"),
                        colorHex = obj.optString("colorHex", "#6750A4"),
                        isPaidThisMonth = obj.optBoolean("isPaidThisMonth", false),
                        notes = obj.optString("notes", ""),
                        websiteUrl = if (obj.has("websiteUrl") && !obj.isNull("websiteUrl")) obj.optString("websiteUrl") else null,
                        notifyEnabled = obj.optBoolean("notifyEnabled", true)
                    )
                )
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun savePayments(payments: List<PaymentItem>) {
        val array = JSONArray()
        for (item in payments) {
            val obj = JSONObject()
            obj.put("id", item.id)
            obj.put("name", item.name)
            obj.put("type", item.type.name)
            obj.put("category", item.category)
            obj.put("amount", item.amount)
            obj.put("currency", item.currency)
            obj.put("startDate", item.startDate)
            if (item.endDate != null) obj.put("endDate", item.endDate)
            obj.put("paymentDayOfMonth", item.paymentDayOfMonth)
            obj.put("paymentTime", item.paymentTime)
            obj.put("iconName", item.iconName)
            obj.put("colorHex", item.colorHex)
            obj.put("isPaidThisMonth", item.isPaidThisMonth)
            obj.put("notes", item.notes)
            if (item.websiteUrl != null) obj.put("websiteUrl", item.websiteUrl)
            obj.put("notifyEnabled", item.notifyEnabled)
            array.put(obj)
        }
        prefs.edit().putString(KEY_PAYMENTS, array.toString()).apply()
    }

    fun getSettings(): AppSettings {
        val budgetVal = if (prefs.contains(KEY_BUDGET_LIMIT)) prefs.getFloat(KEY_BUDGET_LIMIT, -1f) else -1f
        val budgetLimit = if (budgetVal >= 0f) budgetVal.toDouble() else null

        return AppSettings(
            language = prefs.getString(KEY_LANG, "ru") ?: "ru",
            themeMode = prefs.getString(KEY_THEME, "SYSTEM") ?: "SYSTEM",
            accentColor = prefs.getString(KEY_ACCENT, "WALLPAPER") ?: "WALLPAPER",
            notificationsEnabled = prefs.getBoolean(KEY_NOTIF, true),
            isFirstLaunch = prefs.getBoolean(KEY_FIRST_LAUNCH, true),
            monthlyBudgetLimit = budgetLimit
        )
    }

    fun saveSettings(settings: AppSettings) {
        val editor = prefs.edit()
            .putString(KEY_LANG, settings.language)
            .putString(KEY_THEME, settings.themeMode)
            .putString(KEY_ACCENT, settings.accentColor)
            .putBoolean(KEY_NOTIF, settings.notificationsEnabled)
            .putBoolean(KEY_FIRST_LAUNCH, settings.isFirstLaunch)

        if (settings.monthlyBudgetLimit != null) {
            editor.putFloat(KEY_BUDGET_LIMIT, settings.monthlyBudgetLimit.toFloat())
        } else {
            editor.remove(KEY_BUDGET_LIMIT)
        }

        editor.apply()
    }
}