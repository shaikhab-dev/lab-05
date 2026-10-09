
package com.example.listycity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.listycity.ui.theme.ListyCityTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Connect the app to the city repository
        val cityRepository = CityRepository()

        setContent {
            ListyCityTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    CityListScreen(
                        cities = cityRepository.cities,

                        // Add a new city to Firestore
                        onAddCity = { city ->
                            cityRepository.addCity(city)
                        },

                        // Update an existing city
                        onUpdateCity = { oldCity, updatedCity ->
                            cityRepository.updateCity(oldCity, updatedCity)
                        },

                        // Delete a city from Firestore
                        onRemoveCity = { city ->
                            cityRepository.removeCity(city)
                        },

                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
