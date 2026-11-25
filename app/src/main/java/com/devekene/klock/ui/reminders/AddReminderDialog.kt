package com.devekene.klock.ui.reminders

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReminderDialog(
    onDismiss: () -> Unit,
    onSave: (String, Long) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var hoursOffset by remember { mutableStateOf("1") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Reminder", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Reminder Title / Appointment") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = hoursOffset,
                    onValueChange = { hoursOffset = it },
                    label = { Text("In how many hours?") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val hours = hoursOffset.toLongOrNull() ?: 1L
                    val targetTime = System.currentTimeMillis() + (hours * 3600 * 1000L)
                    onSave(if (title.isBlank()) "Appointment" else title, targetTime)
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
