package com.devdd.mypayments.data

import androidx.compose.runtime.Immutable
import java.util.UUID

@Immutable
data class PaymentItem(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val type: ExpenseType,
    val category: String,
    val amount: Double,
    val currency: String = "₽",
    val startDate: Long = System.currentTimeMillis(),
    val endDate: Long? = null, // Expiration date for subscription
    val paymentDayOfMonth: Int = 1,
    val paymentTime: String = "10:00",
    val iconName: String = "star",
    val colorHex: String = "#6750A4",
    val isPaidThisMonth: Boolean = false,
    val notes: String = "",
    val websiteUrl: String? = null, // Direct link to manage/cancel subscription
    val notifyEnabled: Boolean = true
)