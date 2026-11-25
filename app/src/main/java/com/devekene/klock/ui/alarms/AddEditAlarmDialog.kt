package com.devekene.klock.ui.alarms

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.devekene.klock.data.AlarmEntity
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditAlarmDialog(
    onDismiss: () -> Unit,
    onSave: (AlarmEntity) -> Unit
) {
    val calendar = Calendar.getInstance()
    var hour by remember { mutableStateOf(calendar.get(Calendar.HOUR_OF_DAY)) }
    var minute by remember { mutableStateOf(calendar.get(Calendar.MINUTE)) }
    var label by remember { mutableStateOf("Alarm") }
    var scheduleType by remember { mutableStateOf("DAILY") }
    var isGentleWake by remember { mutableStateOf(true) }

    val scheduleOptions = listOf("ONCE", "DAILY", "WEEKDAYS", "WEEKENDS")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Add Alarm", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = hour.toString(),
                        onValueChange = {
                            val h = it.toIntOrNull()
                            if (h != null && h in 0..23) hour = h
                        },
                        label = { Text("Hour (0-23)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.width(110.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(":", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(12.dp))
                    OutlinedTextField(
                        value = minute.toString(),
                        onValueChange = {
                            val m = it.toIntOrNull()
                            if (m != null && m in 0..59) minute = m
                        },
                        label = { Text("Minute (0-59)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.width(110.dp)
                    )
                }

                OutlinedTextField(
                    value = label,
                    onValueChange = { label = it },
                    label = { Text("Alarm Label") },
                    modifier = Modifier.fillMaxWidth()
                )

                Column {
                    Text(text = "Repeat Schedule", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        scheduleOptions.forEach { type ->
                            FilterChip(
                                selected = scheduleType == type,
                                onClick = { scheduleType = type },
                                label = { Text(type, style = MaterialTheme.typography.bodySmall) }
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Gentle Wake-up", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                        Text(text = "Gradually increase alarm volume", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                    }
                    Switch(
                        checked = isGentleWake,
                        onCheckedChange = { isGentleWake = it }
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        AlarmEntity(
                            hour = hour,
                            minute = minute,
                            label = if (label.isBlank()) "Alarm" else label,
                            isEnabled = true,
                            scheduleType = scheduleType,
                            isGentleWake = isGentleWake
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
