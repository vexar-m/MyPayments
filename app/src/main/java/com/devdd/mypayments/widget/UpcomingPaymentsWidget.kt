package com.devdd.mypayments.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import com.devdd.mypayments.MainActivity
import com.devdd.mypayments.R
import com.devdd.mypayments.data.PaymentRepository
import com.devdd.mypayments.util.Formatters

class UpcomingPaymentsWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {
        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val repository = PaymentRepository(context)
            val upcoming = repository.getPayments()
                .filter { !it.isPaidThisMonth }
                .sortedBy { it.paymentDayOfMonth }
                .take(3)

            val views = RemoteViews(context.packageName, R.layout.widget_upcoming_payments)

            // Open app on widget tap
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)

            if (upcoming.isEmpty()) {
                views.setViewVisibility(R.id.widget_empty_text, View.VISIBLE)
                views.setViewVisibility(R.id.item1_layout, View.GONE)
                views.setViewVisibility(R.id.item2_layout, View.GONE)
                views.setViewVisibility(R.id.item3_layout, View.GONE)
            } else {
                views.setViewVisibility(R.id.widget_empty_text, View.GONE)

                val itemLayouts = listOf(R.id.item1_layout, R.id.item2_layout, R.id.item3_layout)
                val itemNames = listOf(R.id.item1_name, R.id.item2_name, R.id.item3_name)
                val itemDays = listOf(R.id.item1_day, R.id.item2_day, R.id.item3_day)
                val itemAmounts = listOf(R.id.item1_amount, R.id.item2_amount, R.id.item3_amount)

                for (i in 0..2) {
                    if (i < upcoming.size) {
                        val item = upcoming[i]
                        views.setViewVisibility(itemLayouts[i], View.VISIBLE)
                        views.setTextViewText(itemNames[i], item.name)
                        views.setTextViewText(itemDays[i], "${item.paymentDayOfMonth}-е ч.")
                        views.setTextViewText(itemAmounts[i], Formatters.formatCurrency(item.amount, item.currency))
                    } else {
                        views.setViewVisibility(itemLayouts[i], View.GONE)
                    }
                }
            }

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        fun updateAllWidgets(context: Context) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val componentName = ComponentName(context, UpcomingPaymentsWidget::class.java)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
            for (id in appWidgetIds) {
                updateAppWidget(context, appWidgetManager, id)
            }
        }
    }
}