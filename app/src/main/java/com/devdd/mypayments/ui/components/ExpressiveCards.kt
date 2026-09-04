package com.devdd.mypayments.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devdd.mypayments.R
import com.devdd.mypayments.data.ExpenseType
import com.devdd.mypayments.data.PaymentItem
import com.devdd.mypayments.util.Formatters

@Composable
fun SummaryCard(
    totalMonthly: Double,
    subscriptionsTotal: Double,
    billsTotal: Double,
    gamesTotal: Double,
    budgetLimit: Double? = null,
    currency: String = "₽",
    modifier: Modifier = Modifier
) {
    val formattedTotal = remember(totalMonthly, currency) {
        Formatters.formatCurrency(totalMonthly, currency)
    }

    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.summary_total_monthly),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                )

                if (budgetLimit != null) {
                    val isExceeded = subscriptionsTotal > budgetLimit
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isExceeded) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
                    ) {
                        Text(
                            text = "Лимит: ${Formatters.formatCurrency(budgetLimit, currency)}",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isExceeded) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = formattedTotal,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            if (budgetLimit != null && budgetLimit > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                val progress = (subscriptionsTotal / budgetLimit).coerceIn(0.0, 1.0).toFloat()
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = if (subscriptionsTotal > budgetLimit) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SummaryCategoryPill(
                    title = stringResource(R.string.summary_subscriptions),
                    amount = Formatters.formatCurrency(subscriptionsTotal, currency),
                    color = MaterialTheme.colorScheme.primary
                )
                SummaryCategoryPill(
                    title = stringResource(R.string.summary_bills),
                    amount = Formatters.formatCurrency(billsTotal, currency),
                    color = MaterialTheme.colorScheme.secondary
                )
                SummaryCategoryPill(
                    title = stringResource(R.string.summary_games),
                    amount = Formatters.formatCurrency(gamesTotal, currency),
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

@Composable
private fun SummaryCategoryPill(
    title: String,
    amount: String,
    color: Color
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.65f),
        modifier = Modifier.padding(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = amount,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
        }
    }
}

@Composable
fun FilterChipsRow(
    selectedFilter: ExpenseType?,
    onFilterSelected: (ExpenseType?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selectedFilter == null,
            onClick = { onFilterSelected(null) },
            label = { Text(stringResource(R.string.type_all)) }
        )
        FilterChip(
            selected = selectedFilter == ExpenseType.SUBSCRIPTION,
            onClick = { onFilterSelected(ExpenseType.SUBSCRIPTION) },
            label = { Text(stringResource(R.string.type_subscriptions)) }
        )
        FilterChip(
            selected = selectedFilter == ExpenseType.MONTHLY_BILL,
            onClick = { onFilterSelected(ExpenseType.MONTHLY_BILL) },
            label = { Text(stringResource(R.string.type_bills)) }
        )
        FilterChip(
            selected = selectedFilter == ExpenseType.GAME_LIMIT,
            onClick = { onFilterSelected(ExpenseType.GAME_LIMIT) },
            label = { Text(stringResource(R.string.type_games)) }
        )
    }
}

@Composable
fun PaymentItemCard(
    item: PaymentItem,
    onTogglePaid: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var menuExpanded by remember { mutableStateOf(false) }

    val parseColor = remember(item.colorHex) {
        try {
            Color(android.graphics.Color.parseColor(item.colorHex))
        } catch (e: Exception) {
            Color(0xFF6750A4)
        }
    }

    val formattedAmount = remember(item.amount, item.currency) {
        Formatters.formatCurrency(item.amount, item.currency)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Icon Avatar
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(parseColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    PaymentIcon(
                        iconName = item.iconName,
                        contentDescription = item.name,
                        modifier = Modifier.size(24.dp),
                        tint = parseColor
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Name & Billing info
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 16.sp),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    val billingText = if (item.type == ExpenseType.GAME_LIMIT) {
                        stringResource(R.string.limit_usage, formattedAmount)
                    } else {
                        stringResource(R.string.renews_on, item.paymentDayOfMonth, item.paymentTime)
                    }

                    Text(
                        text = billingText,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (item.endDate != null && item.type == ExpenseType.SUBSCRIPTION) {
                        Text(
                            text = stringResource(R.string.expires_on, Formatters.formatDate(item.endDate)),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error,
                            maxLines = 1
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Price & Action Menu
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = formattedAmount,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp),
                        color = MaterialTheme.colorScheme.primary
                    )

                    Box {
                        IconButton(
                            onClick = { menuExpanded = true },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Menu",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        if (item.isPaidThisMonth)
                                            stringResource(R.string.btn_mark_unpaid)
                                        else
                                            stringResource(R.string.btn_mark_paid)
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = if (item.isPaidThisMonth) Icons.Default.PendingActions else Icons.Default.CheckCircle,
                                        contentDescription = null
                                    )
                                },
                                onClick = {
                                    menuExpanded = false
                                    onTogglePaid()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.btn_edit)) },
                                leadingIcon = {
                                    Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                                },
                                onClick = {
                                    menuExpanded = false
                                    onEdit()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.btn_delete), color = MaterialTheme.colorScheme.error) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                },
                                onClick = {
                                    menuExpanded = false
                                    onDelete()
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Type badge & Direct Website Manage Button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val typeLabel = when (item.type) {
                        ExpenseType.SUBSCRIPTION -> stringResource(R.string.type_subscriptions)
                        ExpenseType.MONTHLY_BILL -> stringResource(R.string.type_bills)
                        ExpenseType.GAME_LIMIT -> stringResource(R.string.type_games)
                    }

                    SuggestionChip(
                        onClick = { },
                        label = { Text(text = typeLabel, style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp)) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
                        )
                    )

                    // One-click Button to Open Subscription Management Website
                    if (!item.websiteUrl.isNullOrBlank()) {
                        AssistChip(
                            onClick = {
                                try {
                                    val url = item.websiteUrl
                                    val formattedUrl = if (url.startsWith("http")) url else "https://$url"
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(formattedUrl))
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            },
                            label = { Text("Управление", style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp)) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.OpenInNew,
                                    contentDescription = null,
                                    modifier = Modifier.size(13.dp)
                                )
                            },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        )
                    }
                }

                // Paid status chip
                FilterChip(
                    selected = item.isPaidThisMonth,
                    onClick = onTogglePaid,
                    label = {
                        Text(
                            text = if (item.isPaidThisMonth) stringResource(R.string.status_paid) else stringResource(R.string.status_unpaid),
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = if (item.isPaidThisMonth) Icons.Default.CheckCircle else Icons.Default.PendingActions,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }

            if (item.type == ExpenseType.GAME_LIMIT) {
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { if (item.isPaidThisMonth) 1.0f else 0.4f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = parseColor
                )
            }
        }
    }
}