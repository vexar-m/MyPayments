package com.devdd.mypayments.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.devdd.mypayments.data.AppSettings
import com.devdd.mypayments.data.ExpenseType
import com.devdd.mypayments.data.PaymentItem
import com.devdd.mypayments.data.PaymentRepository
import com.devdd.mypayments.data.ServicePreset
import com.devdd.mypayments.notification.NotificationHelper
import com.devdd.mypayments.util.BatteryOptimizationHelper
import com.devdd.mypayments.widget.UpcomingPaymentsWidget
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PaymentRepository(application)

    private val _payments = MutableStateFlow<List<PaymentItem>>(emptyList())
    val payments: StateFlow<List<PaymentItem>> = _payments.asStateFlow()

    private val _settings = MutableStateFlow(AppSettings())
    val settings: StateFlow<AppSettings> = _settings.asStateFlow()

    private val _selectedFilter = MutableStateFlow<ExpenseType?>(null) // null = ALL
    val selectedFilter: StateFlow<ExpenseType?> = _selectedFilter.asStateFlow()

    private val _isBatterySaverDisabled = MutableStateFlow(false)
    val isBatterySaverDisabled: StateFlow<Boolean> = _isBatterySaverDisabled.asStateFlow()

    init {
        loadData()
        checkBatteryOptimizationStatus()
    }

    fun loadData() {
        val loadedPayments = repository.getPayments()
        _payments.value = loadedPayments
        _settings.value = repository.getSettings()
    }

    fun checkBatteryOptimizationStatus() {
        _isBatterySaverDisabled.value =
            BatteryOptimizationHelper.isIgnoringBatteryOptimizations(getApplication())
    }

    fun setFilter(filter: ExpenseType?) {
        _selectedFilter.value = filter
    }

    fun completeOnboarding(
        budget: Double?,
        initialPresets: List<ServicePreset>
    ) {
        val newSettings = _settings.value.copy(
            isFirstLaunch = false,
            monthlyBudgetLimit = budget
        )
        updateSettings(newSettings)

        val newItems = initialPresets.map { preset ->
            PaymentItem(
                name = preset.name,
                type = preset.defaultType,
                category = preset.category,
                amount = preset.defaultAmount,
                currency = preset.currency,
                paymentDayOfMonth = 1,
                paymentTime = "10:00",
                iconName = preset.iconName,
                colorHex = preset.colorHex,
                websiteUrl = preset.websiteUrl
            )
        }

        if (newItems.isNotEmpty()) {
            val current = _payments.value.toMutableList()
            current.addAll(0, newItems)
            _payments.value = current
            repository.savePayments(current)
            
            newItems.forEach {
                NotificationHelper.schedulePaymentNotification(getApplication(), it)
            }
        }

        UpcomingPaymentsWidget.updateAllWidgets(getApplication())
    }

    /**
     * Checks if adding this payment would exceed the configured monthly budget limit.
     */
    fun wouldExceedBudget(item: PaymentItem): Boolean {
        val budget = _settings.value.monthlyBudgetLimit ?: return false
        val currentTotal = _payments.value
            .filter { it.type == ExpenseType.SUBSCRIPTION && it.id != item.id }
            .sumOf { it.amount }

        return (currentTotal + item.amount) > budget
    }

    fun addOrUpdatePayment(item: PaymentItem) {
        val currentList = _payments.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == item.id }
        if (index >= 0) {
            currentList[index] = item
        } else {
            currentList.add(0, item)
        }
        _payments.value = currentList
        repository.savePayments(currentList)

        // Schedule push notification reminder
        NotificationHelper.schedulePaymentNotification(getApplication(), item)

        // Update home screen widget
        UpcomingPaymentsWidget.updateAllWidgets(getApplication())
    }

    fun deletePayment(item: PaymentItem) {
        val currentList = _payments.value.filter { it.id != item.id }
        _payments.value = currentList
        repository.savePayments(currentList)

        UpcomingPaymentsWidget.updateAllWidgets(getApplication())
    }

    fun togglePaidStatus(item: PaymentItem) {
        val updated = item.copy(isPaidThisMonth = !item.isPaidThisMonth)
        addOrUpdatePayment(updated)
    }

    fun updateSettings(newSettings: AppSettings) {
        _settings.value = newSettings
        repository.saveSettings(newSettings)
    }

    fun sendTestNotification() {
        NotificationHelper.sendTestNotification(getApplication())
    }

    fun getTotalMonthlyExpense(): Double {
        return _payments.value.sumOf { it.amount }
    }

    fun getCategoryTotal(type: ExpenseType): Double {
        return _payments.value.filter { it.type == type }.sumOf { it.amount }
    }
}