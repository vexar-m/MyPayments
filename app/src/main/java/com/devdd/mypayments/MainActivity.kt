package com.devdd.mypayments

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.devdd.mypayments.notification.NotificationHelper
import com.devdd.mypayments.ui.MainViewModel
import com.devdd.mypayments.ui.screens.AnalyticsScreen
import com.devdd.mypayments.ui.screens.HomeScreen
import com.devdd.mypayments.ui.screens.OnboardingScreen
import com.devdd.mypayments.ui.screens.SettingsScreen
import com.devdd.mypayments.ui.theme.MyPaymentsTheme
import com.devdd.mypayments.util.LocaleHelper

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences("my_payments_prefs", Context.MODE_PRIVATE)
        val lang = prefs.getString("key_language", "ru") ?: "ru"
        val context = LocaleHelper.setLocale(newBase, lang)
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NotificationHelper.createNotificationChannel(this)

        setContent {
            val settings by viewModel.settings.collectAsState()

            MyPaymentsTheme(
                themeMode = settings.themeMode,
                accentColor = settings.accentColor
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    if (settings.isFirstLaunch) {
                        OnboardingScreen(
                            viewModel = viewModel,
                            onComplete = {
                                viewModel.loadData()
                            }
                        )
                    } else {
                        MainAppScaffold(viewModel = viewModel)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScaffold(viewModel: MainViewModel) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (selectedTab) {
                            0 -> stringResource(R.string.app_name)
                            1 -> stringResource(R.string.nav_analytics)
                            else -> stringResource(R.string.settings_title)
                        },
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors()
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(imageVector = Icons.Default.Home, contentDescription = null) },
                    label = { Text(stringResource(R.string.nav_home)) }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(imageVector = Icons.Default.Analytics, contentDescription = null) },
                    label = { Text(stringResource(R.string.nav_analytics)) }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(imageVector = Icons.Default.Settings, contentDescription = null) },
                    label = { Text(stringResource(R.string.nav_settings)) }
                )
            }
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        Crossfade(targetState = selectedTab, label = "TabTransition") { tab ->
            when (tab) {
                0 -> HomeScreen(viewModel = viewModel, modifier = modifier)
                1 -> AnalyticsScreen(viewModel = viewModel, modifier = modifier)
                2 -> SettingsScreen(viewModel = viewModel, modifier = modifier)
            }
        }
    }
}