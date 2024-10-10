package com.example.flightsearchapp

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.flightsearchapp.data.AppContainer
import com.example.flightsearchapp.data.DefaultAppContainer


private const val SEARCH_PREFERENCES_NAME = "search_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = SEARCH_PREFERENCES_NAME,
)

class FlightSearchApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this, dataStore)
    }
}