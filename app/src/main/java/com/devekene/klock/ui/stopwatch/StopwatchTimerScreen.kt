package com.devekene.klock.ui.stopwatch

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.devekene.klock.viewmodel.KlockViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StopwatchTimerScreen(
    viewModel: KlockViewModel
) {
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        TextButton(onClick = { selectedTab = 0 }) {
                            Text(
                                "Stopwatch",
                                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                            )
                        }
                        TextButton(onClick = { selectedTab = 1 }) {
                            Text(
                                "Timer",
                                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (selectedTab == 0) {
                StopwatchContent(viewModel = viewModel)
            } else {
                TimerContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun StopwatchContent(viewModel: KlockViewModel) {
    val timeMillis by viewModel.stopwatchTime.collectAsState()
    val isRunning by viewModel.isStopwatchRunning.collectAsState()
    val laps by viewModel.laps.collectAsState()

    val minutes = (timeMillis / 60000) % 60
    val seconds = (timeMillis / 1000) % 60
    val millis = (timeMillis % 1000) / 10

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = String.format(Locale.getDefault(), "%02d:%02d.%02d", minutes, seconds, millis),
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { viewModel.resetStopwatch() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Text("Reset", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Button(
                onClick = { if (isRunning) viewModel.pauseStopwatch() else viewModel.startStopwatch() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(if (isRunning) "Pause" else "Start", color = MaterialTheme.colorScheme.onPrimary)
            }

            Button(
                onClick = { viewModel.lapStopwatch() },
                enabled = isRunning,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Text("Lap", color = MaterialTheme.colorScheme.onSecondaryContainer)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(laps) { index, lapTime ->
                val lapMin = (lapTime / 60000) % 60
                val lapSec = (lapTime / 1000) % 60
                val lapMs = (lapTime % 1000) / 10
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Lap ${laps.size - index}", fontWeight = FontWeight.SemiBold)
                        Text(text = String.format(Locale.getDefault(), "%02d:%02d.%02d", lapMin, lapSec, lapMs), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun TimerContent(viewModel: KlockViewModel) {
    val duration by viewModel.timerDuration.collectAsState()
    val remaining by viewModel.timerRemaining.collectAsState()
    val isRunning by viewModel.isTimerRunning.collectAsState()

    val displayTime = if (remaining > 0L) remaining else duration
    val hours = (displayTime / 3600000)
    val minutes = (displayTime / 60000) % 60
    val seconds = (displayTime / 1000) % 60

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds),
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(24.dp))

        if (!isRunning && remaining == 0L) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(onClick = { viewModel.startTimer(1 * 60 * 1000L) }) { Text("1 Min") }
                OutlinedButton(onClick = { viewModel.startTimer(5 * 60 * 1000L) }) { Text("5 Min") }
                OutlinedButton(onClick = { viewModel.startTimer(15 * 60 * 1000L) }) { Text("15 Min") }
                OutlinedButton(onClick = { viewModel.startTimer(30 * 60 * 1000L) }) { Text("30 Min") }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { viewModel.resetTimer() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Text("Reset", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Button(
                onClick = {
                    if (isRunning) {
                        viewModel.pauseTimer()
                    } else if (remaining > 0L) {
                        viewModel.resumeTimer()
                    } else {
                        viewModel.startTimer(5 * 60 * 1000L)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(if (isRunning) "Pause" else "Start", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}
