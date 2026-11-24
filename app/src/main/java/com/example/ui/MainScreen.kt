package com.example.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessAlarm
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.ui.alarms.AddEditAlarmDialog
import com.example.ui.alarms.AlarmScreen
import com.example.ui.reminders.AddReminderDialog
import com.example.ui.reminders.RemindersScreen
import com.example.ui.stopwatch.StopwatchTimerScreen
import com.example.ui.worldclock.AddCityDialog
import com.example.ui.worldclock.WorldClockScreen
import com.example.viewmodel.KlockViewModel

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Alarms : Screen("alarms", "Alarms", Icons.Default.AccessAlarm)
    object WorldClock : Screen("world_clock", "World Clock", Icons.Default.Public)
    object StopwatchTimer : Screen("stopwatch_timer", "Stopwatch", Icons.Default.Timer)
    object Reminders : Screen("reminders", "Reminders", Icons.Default.Notifications)
}

@Composable
fun MainScreen(viewModel: KlockViewModel) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Alarms) }
    var showAddAlarmDialog by remember { mutableStateOf(false) }
    var showAddCityDialog by remember { mutableStateOf(false) }
    var showAddReminderDialog by remember { mutableStateOf(false) }

    val items = listOf(
        Screen.Alarms,
        Screen.WorldClock,
        Screen.StopwatchTimer,
        Screen.Reminders
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentScreen == screen,
                        onClick = { currentScreen = screen },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            when (currentScreen) {
                is Screen.Alarms -> AlarmScreen(
                    viewModel = viewModel,
                    onAddAlarmClick = { showAddAlarmDialog = true }
                )
                is Screen.WorldClock -> WorldClockScreen(
                    viewModel = viewModel,
                    onAddCityClick = { showAddCityDialog = true }
                )
                is Screen.StopwatchTimer -> StopwatchTimerScreen(
                    viewModel = viewModel
                )
                is Screen.Reminders -> RemindersScreen(
                    viewModel = viewModel,
                    onAddReminderClick = { showAddReminderDialog = true }
                )
            }
        }
    }

    if (showAddAlarmDialog) {
        AddEditAlarmDialog(
            onDismiss = { showAddAlarmDialog = false },
            onSave = { alarm ->
                viewModel.addAlarm(alarm)
                showAddAlarmDialog = false
            }
        )
    }

    if (showAddCityDialog) {
        AddCityDialog(
            onDismiss = { showAddCityDialog = false },
            onCitySelected = { cityName, timezoneId, country ->
                viewModel.addWorldClock(cityName, timezoneId, country)
                showAddCityDialog = false
            }
        )
    }

    if (showAddReminderDialog) {
        AddReminderDialog(
            onDismiss = { showAddReminderDialog = false },
            onSave = { title, timestamp ->
                viewModel.addReminder(title, timestamp)
                showAddReminderDialog = false
            }
        )
    }
}
