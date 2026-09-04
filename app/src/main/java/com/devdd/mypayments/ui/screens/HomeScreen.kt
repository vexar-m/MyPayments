package com.devdd.mypayments.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.devdd.mypayments.R
import com.devdd.mypayments.data.ExpenseType
import com.devdd.mypayments.data.PaymentItem
import com.devdd.mypayments.ui.MainViewModel
import com.devdd.mypayments.ui.components.AddPaymentBottomSheet
import com.devdd.mypayments.ui.components.FilterChipsRow
import com.devdd.mypayments.ui.components.PaymentItemCard
import com.devdd.mypayments.ui.components.SummaryCard
import com.devdd.mypayments.util.Formatters

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val payments by viewModel.payments.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()

    var showAddBottomSheet by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<PaymentItem?>(null) }
    var pendingItemForBudgetWarning by remember { mutableStateOf<PaymentItem?>(null) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val filteredPayments by remember(payments, selectedFilter) {
        derivedStateOf {
            if (selectedFilter == null) payments
            else payments.filter { it.type == selectedFilter }
        }
    }

    val totalMonthly by remember(payments) { derivedStateOf { payments.sumOf { it.amount } } }
    val subsTotal by remember(payments) { derivedStateOf { viewModel.getCategoryTotal(ExpenseType.SUBSCRIPTION) } }
    val billsTotal by remember(payments) { derivedStateOf { viewModel.getCategoryTotal(ExpenseType.MONTHLY_BILL) } }
    val gamesTotal by remember(payments) { derivedStateOf { viewModel.getCategoryTotal(ExpenseType.GAME_LIMIT) } }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 12.dp, bottom = 88.dp)
        ) {
            item {
                SummaryCard(
                    totalMonthly = totalMonthly,
                    subscriptionsTotal = subsTotal,
                    billsTotal = billsTotal,
                    gamesTotal = gamesTotal,
                    budgetLimit = settings.monthlyBudgetLimit
                )

                Spacer(modifier = Modifier.height(10.dp))

                FilterChipsRow(
                    selectedFilter = selectedFilter,
                    onFilterSelected = { viewModel.setFilter(it) }
                )

                Spacer(modifier = Modifier.height(4.dp))
            }

            if (filteredPayments.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp, bottom = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Payments,
                            contentDescription = null,
                            modifier = Modifier.padding(12.dp),
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                        )
                        Text(
                            text = stringResource(R.string.no_payments_yet),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(
                    items = filteredPayments,
                    key = { it.id }
                ) { item ->
                    PaymentItemCard(
                        item = item,
                        onTogglePaid = { viewModel.togglePaidStatus(item) },
                        onEdit = {
                            editingItem = item
                            showAddBottomSheet = true
                        },
                        onDelete = { viewModel.deletePayment(item) },
                        modifier = Modifier.animateItem()
                    )
                }
            }
        }

        // FAB located in bottom right corner ("кнопка в правом нижнем углу")
        FloatingActionButton(
            onClick = {
                editingItem = null
                showAddBottomSheet = true
            },
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.add_payment)
            )
        }
    }

    if (showAddBottomSheet) {
        AddPaymentBottomSheet(
            sheetState = sheetState,
            initialItem = editingItem,
            onSave = { newItem ->
                if (newItem.type == ExpenseType.SUBSCRIPTION && viewModel.wouldExceedBudget(newItem)) {
                    pendingItemForBudgetWarning = newItem
                    showAddBottomSheet = false
                } else {
                    viewModel.addOrUpdatePayment(newItem)
                    showAddBottomSheet = false
                }
            },
            onDismiss = { showAddBottomSheet = false }
        )
    }

    // Budget Limit Exceeded Warning Dialog
    if (pendingItemForBudgetWarning != null) {
        val item = pendingItemForBudgetWarning!!
        val currentSubsTotal = viewModel.getCategoryTotal(ExpenseType.SUBSCRIPTION)
        val newTotal = currentSubsTotal + item.amount
        val budget = settings.monthlyBudgetLimit ?: 0.0

        AlertDialog(
            onDismissRequest = { pendingItemForBudgetWarning = null },
            icon = {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = {
                Text(
                    text = "Превышение бюджета на подписки!",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Text(
                    text = "Добавление подписки «${item.name}» (${Formatters.formatCurrency(item.amount, item.currency)}) превысит ваш лимит (${Formatters.formatCurrency(budget)}).\n\nИтого подписок: ${Formatters.formatCurrency(newTotal)}.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateSettings(settings.copy(monthlyBudgetLimit = newTotal))
                        viewModel.addOrUpdatePayment(item)
                        pendingItemForBudgetWarning = null
                    }
                ) {
                    Text("Повысить лимит")
                }
            },
            dismissButton = {
                Column {
                    OutlinedButton(
                        onClick = {
                            viewModel.updateSettings(settings.copy(monthlyBudgetLimit = null))
                            viewModel.addOrUpdatePayment(item)
                            pendingItemForBudgetWarning = null
                        }
                    ) {
                        Text("Отключить лимит")
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    TextButton(
                        onClick = {
                            viewModel.addOrUpdatePayment(item)
                            pendingItemForBudgetWarning = null
                        }
                    ) {
                        Text("Сохранить всё равно")
                    }
                }
            }
        )
    }
}