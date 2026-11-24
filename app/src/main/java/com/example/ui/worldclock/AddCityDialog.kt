package com.example.ui.worldclock

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class CityInfo(val name: String, val country: String, val timezoneId: String)

val POPULAR_CITIES = listOf(
    CityInfo("New York", "United States", "America/New_York"),
    CityInfo("London", "United Kingdom", "Europe/London"),
    CityInfo("Tokyo", "Japan", "Asia/Tokyo"),
    CityInfo("Paris", "France", "Europe/Paris"),
    CityInfo("Sydney", "Australia", "Australia/Sydney"),
    CityInfo("Dubai", "United Arab Emirates", "Asia/Dubai"),
    CityInfo("Singapore", "Singapore", "Asia/Singapore"),
    CityInfo("Los Angeles", "United States", "America/Los_Angeles"),
    CityInfo("Berlin", "Germany", "Europe/Berlin"),
    CityInfo("Toronto", "Canada", "America/Toronto")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCityDialog(
    onDismiss: () -> Unit,
    onCitySelected: (String, String, String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredCities = POPULAR_CITIES.filter {
        it.name.contains(searchQuery, ignoreCase = true) || it.country.contains(searchQuery, ignoreCase = true)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add World Clock", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search city...") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(filteredCities) { city ->
                        ListItem(
                            headlineContent = { Text(city.name, fontWeight = FontWeight.SemiBold) },
                            supportingContent = { Text(city.country) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onCitySelected(city.name, city.timezoneId, city.country)
                                }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
