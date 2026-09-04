package com.devdd.mypayments.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.devdd.mypayments.R

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val itemName = intent.getStringExtra("item_name") ?: "Платеж"
        val itemAmount = intent.getStringExtra("item_amount") ?: ""
        val itemTime = intent.getStringExtra("item_time") ?: "10:00"

        val title = context.getString(R.string.notif_payment_due_title, itemName)
        val body = context.getString(R.string.notif_payment_due_body, itemName, itemAmount, itemTime)

        val id = intent.getStringExtra("item_id")?.hashCode() ?: System.currentTimeMillis().toInt()
        NotificationHelper.sendPaymentDueNotification(context, id, title, body)
    }
}