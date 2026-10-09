
package com.example.listycity

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listycity.ui.theme.ListyCityTheme

@Composable
fun CityListScreen(
    cities: List<City>,
    onAddCity: (City) -> Unit,
    onUpdateCity: (City, City) -> Unit,
    onRemoveCity: (City) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedCity by remember { mutableStateOf<City?>(null) }
    var cityToDelete by remember { mutableStateOf<City?>(null) }

    var cityName by remember { mutableStateOf("") }
    var provinceName by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Title and add button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "My Cities",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "${cities.size} cities saved",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            FloatingActionButton(
                onClick = {
                    cityName = ""
                    provinceName = ""
                    showAddDialog = true
                }
            ) {
                Text("+", fontSize = 26.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Display cities
        if (cities.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No cities yet. Tap + to add one!")
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(cities, key = { it.name }) { city ->
                    CityRow(
                        city = city,
                        onClick = {
                            selectedCity = city
                            cityName = city.name
                            provinceName = city.province
                        }
                    )
                }
            }
        }
    }

    // Add a city
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add City") },
            text = {
                Column {
                    OutlinedTextField(
                        value = cityName,
                        onValueChange = { cityName = it },
                        label = { Text("City name") },
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = provinceName,
                        onValueChange = { provinceName = it },
                        label = { Text("Province") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(
                    enabled = cityName.isNotBlank() &&
                            provinceName.isNotBlank(),
                    onClick = {
                        onAddCity(
                            City(
                                cityName.trim(),
                                provinceName.trim().uppercase()
                            )
                        )
                        showAddDialog = false
                    }
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Edit a city
    selectedCity?.let { city ->
        AlertDialog(
            onDismissRequest = { selectedCity = null },
            title = { Text("Edit City") },
            text = {
                Column {
                    OutlinedTextField(
                        value = cityName,
                        onValueChange = { cityName = it },
                        label = { Text("City name") },
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = provinceName,
                        onValueChange = { provinceName = it },
                        label = { Text("Province") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(
                    enabled = cityName.isNotBlank() &&
                            provinceName.isNotBlank(),
                    onClick = {
                        onUpdateCity(
                            city,
                            City(
                                cityName.trim(),
                                provinceName.trim().uppercase()
                            )
                        )
                        selectedCity = null
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                Row {
                    TextButton(
                        onClick = {
                            cityToDelete = city
                            selectedCity = null
                        }
                    ) {
                        Text(
                            "Delete",
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    TextButton(onClick = { selectedCity = null }) {
                        Text("Cancel")
                    }
                }
            }
        )
    }

    // Confirm deletion
    cityToDelete?.let { city ->
        AlertDialog(
            onDismissRequest = { cityToDelete = null },
            title = { Text("Delete city?") },
            text = {
                Text(
                    "Are you sure you want to delete " +
                            "${city.name}, ${city.province}?"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onRemoveCity(city)
                        cityToDelete = null
                    }
                ) {
                    Text(
                        "Delete",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { cityToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun CityRow(
    city: City,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = city.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = city.province,
                    fontSize = 14.sp
                )
            }

            Text(
                text = "Edit ›",
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CityListScreenPreview() {
    ListyCityTheme {
        CityListScreen(
            cities = listOf(
                City("Calgary", "AB"),
                City("Edmonton", "AB"),
                City("Montreal", "QC"),
                City("Vancouver", "BC"),
                City("Victoria", "BC")
            ),
            onAddCity = {},
            onUpdateCity = { _, _ -> },
            onRemoveCity = {}
        )
    }
}
