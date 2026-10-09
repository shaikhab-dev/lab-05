
package com.example.listycity

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class CityRepository {

    private val db = Firebase.firestore
    private val citiesRef = db.collection("cities")

    // Cities currently displayed in the app
    private val _cities = mutableStateListOf<City>()

    val cities: List<City>
        get() = _cities

    init {
        // Listen for changes in Firestore
        citiesRef.addSnapshotListener { snapshot, error ->

            if (error != null) {
                Log.e("CityRepository", "Error loading cities", error)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val updatedCities = snapshot.documents
                    .mapNotNull { document ->
                        document.toObject(City::class.java)
                    }
                    .sortedBy { it.name.lowercase() }

                _cities.clear()
                _cities.addAll(updatedCities)
            }
        }
    }

    fun addCity(city: City) {
        citiesRef.document(city.name).set(city)
            .addOnFailureListener { error ->
                Log.e("CityRepository", "Error adding city", error)
            }
    }

    fun updateCity(oldCity: City, updatedCity: City) {

        if (oldCity.name == updatedCity.name) {
            // Update the existing city
            citiesRef.document(oldCity.name).set(updatedCity)
                .addOnFailureListener { error ->
                    Log.e("CityRepository", "Error updating city", error)
                }
        } else {
            // Rename the document if the city name changes
            val batch = db.batch()

            batch.delete(citiesRef.document(oldCity.name))
            batch.set(citiesRef.document(updatedCity.name), updatedCity)

            batch.commit()
                .addOnFailureListener { error ->
                    Log.e("CityRepository", "Error renaming city", error)
                }
        }
    }

    fun removeCity(city: City) {
        // Delete the city from Firestore
        citiesRef.document(city.name).delete()
            .addOnFailureListener { error ->
                Log.e("CityRepository", "Error deleting city", error)
            }
    }
}
