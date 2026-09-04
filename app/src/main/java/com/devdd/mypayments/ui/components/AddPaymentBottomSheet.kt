package com.devdd.mypayments.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.SheetState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.devdd.mypayments.R
import com.devdd.mypayments.data.ExpenseType
import com.devdd.mypayments.data.PaymentItem
import com.devdd.mypayments.data.Presets
import com.devdd.mypayments.data.ServicePreset
import com.devdd.mypayments.util.Formatters
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddPaymentBottomSheet(
    sheetState: SheetState,
    initialItem: PaymentItem? = null,
    onSave: (PaymentItem) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedType by remember {
        mutableStateOf(initialItem?.type ?: ExpenseType.SUBSCRIPTION)
    }

    var selectedPresetCategory by remember { mutableStateOf("Creators") }

    var name by remember { mutableStateOf(initialItem?.name ?: "") }
    var amount by remember { mutableStateOf(initialItem?.amount?.toInt()?.toString() ?: "500") }
    var currency by remember { mutableStateOf(initialItem?.currency ?: "₽") }
    var category by remember { mutableStateOf(initialItem?.category ?: "Other") }
    var paymentDay by remember { mutableIntStateOf(initialItem?.paymentDayOfMonth ?: 1) }
    var paymentTime by remember { mutableStateOf(initialItem?.paymentTime ?: "10:00") }
    var startDate by remember { mutableStateOf(initialItem?.startDate ?: System.currentTimeMillis()) }
    var endDate by remember { mutableStateOf<Long?>(initialItem?.endDate) }
    var selectedIntervalOption by remember { mutableStateOf("1_MONTH") }
    var iconName by remember { mutableStateOf(initialItem?.iconName ?: "star") }
    var colorHex by remember { mutableStateOf(initialItem?.colorHex ?: "#6750A4") }
    var notes by remember { mutableStateOf(initialItem?.notes ?: "") }
    var websiteUrl by remember { mutableStateOf(initialItem?.websiteUrl ?: "") }

    var showDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var isManualMode by remember { mutableStateOf(initialItem != null) }

    val presetColors = listOf(
        "#F15A24", "#0077FF", "#10A37F", "#2563EB", "#D97706",
        "#E50914", "#8B5CF6", "#0284C7", "#10B981", "#EC4899", "#171A21"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = if (initialItem != null) stringResource(R.string.btn_edit) else stringResource(R.string.add_payment),
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Step 1: Expense Type Selector Tabs (Subscription / Bills / Game Limits)
            PrimaryTabRow(
                selectedTabIndex = selectedType.ordinal,
                modifier = Modifier.fillMaxWidth()
            ) {
                Tab(
                    selected = selectedType == ExpenseType.SUBSCRIPTION,
                    onClick = {
                        selectedType = ExpenseType.SUBSCRIPTION
                        category = "Creators"
                    },
                    text = { Text(stringResource(R.string.type_subscriptions)) }
                )
                Tab(
                    selected = selectedType == ExpenseType.MONTHLY_BILL,
                    onClick = {
                        selectedType = ExpenseType.MONTHLY_BILL
                        category = "Utilities"
                    },
                    text = { Text(stringResource(R.string.type_bills)) }
                )
                Tab(
                    selected = selectedType == ExpenseType.GAME_LIMIT,
                    onClick = {
                        selectedType = ExpenseType.GAME_LIMIT
                        category = "Games"
                    },
                    text = { Text(stringResource(R.string.type_games)) }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Presets Selection
            if (!isManualMode && initialItem == null) {
                Text(
                    text = stringResource(R.string.select_category),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )

                Spacer(modifier = Modifier.height(8.dp))

                val presetList: List<ServicePreset> = when (selectedType) {
                    ExpenseType.SUBSCRIPTION -> {
                        when (selectedPresetCategory) {
                            "Creators" -> Presets.creatorsPresets
                            "Social" -> Presets.socialPresets
                            "AI" -> Presets.aiPresets
                            "Music" -> Presets.musicPresets
                            "Movies" -> Presets.moviePresets
                            else -> Presets.cloudPresets
                        }
                    }
                    ExpenseType.MONTHLY_BILL -> Presets.utilityPresets
                    ExpenseType.GAME_LIMIT -> Presets.gamePresets
                }

                if (selectedType == ExpenseType.SUBSCRIPTION) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item {
                            FilterChip(
                                selected = selectedPresetCategory == "Creators",
                                onClick = { selectedPresetCategory = "Creators" },
                                label = { Text("Бусти & Авторы") }
                            )
                        }
                        item {
                            FilterChip(
                                selected = selectedPresetCategory == "Social",
                                onClick = { selectedPresetCategory = "Social" },
                                label = { Text("Соцсети & Премиум") }
                            )
                        }
                        item {
                            FilterChip(
                                selected = selectedPresetCategory == "AI",
                                onClick = { selectedPresetCategory = "AI" },
                                label = { Text(stringResource(R.string.presets_ai)) }
                            )
                        }
                        item {
                            FilterChip(
                                selected = selectedPresetCategory == "Music",
                                onClick = { selectedPresetCategory = "Music" },
                                label = { Text(stringResource(R.string.presets_music)) }
                            )
                        }
                        item {
                            FilterChip(
                                selected = selectedPresetCategory == "Movies",
                                onClick = { selectedPresetCategory = "Movies" },
                                label = { Text(stringResource(R.string.presets_movies)) }
                            )
                        }
                        item {
                            FilterChip(
                                selected = selectedPresetCategory == "Cloud",
                                onClick = { selectedPresetCategory = "Cloud" },
                                label = { Text(stringResource(R.string.presets_cloud)) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // Grid/Row of Presets
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(presetList) { preset ->
                        val presetColor = try {
                            Color(android.graphics.Color.parseColor(preset.colorHex))
                        } catch (e: Exception) {
                            MaterialTheme.colorScheme.primary
                        }

                        ElevatedCard(
                            onClick = {
                                name = preset.name
                                amount = preset.defaultAmount.toInt().toString()
                                currency = preset.currency
                                iconName = preset.iconName
                                colorHex = preset.colorHex
                                category = preset.category
                                websiteUrl = preset.websiteUrl ?: ""
                                isManualMode = true
                            },
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.width(130.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(presetColor.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    PaymentIcon(
                                        iconName = preset.iconName,
                                        contentDescription = preset.name,
                                        modifier = Modifier.size(24.dp),
                                        tint = presetColor
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = preset.name,
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = Formatters.formatCurrency(preset.defaultAmount, preset.currency),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedButton(
                    onClick = { isManualMode = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.presets_manual))
                }

                Spacer(modifier = Modifier.height(14.dp))
            }

            // Custom Details Form
            if (isManualMode || initialItem != null) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.field_name)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        label = {
                            Text(
                                if (selectedType == ExpenseType.GAME_LIMIT)
                                    stringResource(R.string.field_game_budget)
                                else
                                    stringResource(R.string.field_amount)
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    OutlinedTextField(
                        value = currency,
                        onValueChange = { currency = it },
                        label = { Text(stringResource(R.string.field_currency)) },
                        modifier = Modifier.width(90.dp),
                        shape = RoundedCornerShape(14.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Website Management URL Field
                OutlinedTextField(
                    value = websiteUrl,
                    onValueChange = { websiteUrl = it },
                    label = { Text("Ссылка на сайт управления (например, boosty.to)") },
                    leadingIcon = { Icon(imageVector = Icons.Default.Language, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Period / Billing Interval Presets
                Text(
                    text = "Период и расписание подписки",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        FilterChip(
                            selected = selectedIntervalOption == "1_MONTH",
                            onClick = {
                                selectedIntervalOption = "1_MONTH"
                                val cal = Calendar.getInstance().apply { timeInMillis = startDate }
                                cal.add(Calendar.MONTH, 1)
                                endDate = cal.timeInMillis
                            },
                            label = { Text("Каждый месяц") }
                        )
                    }
                    item {
                        FilterChip(
                            selected = selectedIntervalOption == "6_MONTHS",
                            onClick = {
                                selectedIntervalOption = "6_MONTHS"
                                val cal = Calendar.getInstance().apply { timeInMillis = startDate }
                                cal.add(Calendar.MONTH, 6)
                                endDate = cal.timeInMillis
                            },
                            label = { Text("6 месяцев") }
                        )
                    }
                    item {
                        FilterChip(
                            selected = selectedIntervalOption == "1_YEAR",
                            onClick = {
                                selectedIntervalOption = "1_YEAR"
                                val cal = Calendar.getInstance().apply { timeInMillis = startDate }
                                cal.add(Calendar.YEAR, 1)
                                endDate = cal.timeInMillis
                            },
                            label = { Text("1 год") }
                        )
                    }
                    item {
                        FilterChip(
                            selected = selectedIntervalOption == "2_YEARS",
                            onClick = {
                                selectedIntervalOption = "2_YEARS"
                                val cal = Calendar.getInstance().apply { timeInMillis = startDate }
                                cal.add(Calendar.YEAR, 2)
                                endDate = cal.timeInMillis
                            },
                            label = { Text("2 года") }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    InputChip(
                        selected = true,
                        onClick = { showDatePicker = true },
                        label = { Text("Старт: ${Formatters.formatDate(startDate)}") },
                        leadingIcon = { Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = null) },
                        modifier = Modifier.weight(1f)
                    )

                    InputChip(
                        selected = true,
                        onClick = { showTimePicker = true },
                        label = { Text(paymentTime) },
                        leadingIcon = { Icon(imageVector = Icons.Default.Schedule, contentDescription = null) }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Payment Day Selection (1..28, 29, 30, 31)
                Text(
                    text = "День оплаты в месяце: $paymentDay-е число",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                )

                Spacer(modifier = Modifier.height(6.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items((1..31).toList()) { day ->
                        FilterChip(
                            selected = paymentDay == day,
                            onClick = { paymentDay = day },
                            label = { Text("$day") }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Icon Picker Grid
                Text(
                    text = stringResource(R.string.field_icon),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )

                Spacer(modifier = Modifier.height(8.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconPicker.availableIcons.keys.forEach { nameKey ->
                        val isSelected = iconName == nameKey
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primaryContainer
                                    else MaterialTheme.colorScheme.surfaceContainerHighest
                                )
                                .border(
                                    width = if (isSelected) 2.dp else 0.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { iconName = nameKey },
                            contentAlignment = Alignment.Center
                        ) {
                            PaymentIcon(
                                iconName = nameKey,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Color Picker Row
                Text(
                    text = stringResource(R.string.field_color),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(presetColors) { hex ->
                        val isSelected = colorHex.equals(hex, ignoreCase = true)
                        val color = Color(android.graphics.Color.parseColor(hex))
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = if (isSelected) 3.dp else 0.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { colorHex = hex },
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text(stringResource(R.string.field_notes)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))
            }

            // Save & Cancel Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(onClick = onDismiss) {
                    Text(stringResource(R.string.btn_cancel))
                }

                Spacer(modifier = Modifier.width(12.dp))

                Button(
                    onClick = {
                        val parsedAmount = amount.toDoubleOrNull() ?: 0.0
                        val item = PaymentItem(
                            id = initialItem?.id ?: java.util.UUID.randomUUID().toString(),
                            name = if (name.isBlank()) "Новый платеж" else name,
                            type = selectedType,
                            category = category,
                            amount = parsedAmount,
                            currency = currency,
                            startDate = startDate,
                            endDate = endDate,
                            paymentDayOfMonth = paymentDay,
                            paymentTime = paymentTime,
                            iconName = iconName,
                            colorHex = colorHex,
                            isPaidThisMonth = initialItem?.isPaidThisMonth ?: false,
                            notes = notes,
                            websiteUrl = if (websiteUrl.isBlank()) null else websiteUrl
                        )
                        onSave(item)
                    },
                    enabled = name.isNotBlank() || isManualMode
                ) {
                    Text(stringResource(R.string.btn_save))
                }
            }
        }
    }

    // Dialog Launchers
    if (showDatePicker) {
        AppDatePickerDialog(
            initialSelectedDateMs = startDate,
            onDateSelected = { if (it != null) startDate = it },
            onDismiss = { showDatePicker = false }
        )
    }

    if (showEndDatePicker) {
        AppDatePickerDialog(
            initialSelectedDateMs = endDate ?: (System.currentTimeMillis() + 86400000L * 30),
            onDateSelected = { endDate = it },
            onDismiss = { showEndDatePicker = false }
        )
    }

    if (showTimePicker) {
        AppTimePickerDialog(
            initialTime = paymentTime,
            onTimeSelected = { paymentTime = it },
            onDismiss = { showTimePicker = false }
        )
    }
}