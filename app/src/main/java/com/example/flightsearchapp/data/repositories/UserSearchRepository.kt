package com.example.flightsearchapp.data.repositories

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserSearchRepository(private val dataStore: DataStore<Preferences>) {

    private companion object {
        val SEARCH_QUERY_KEY = stringPreferencesKey("search_query")
        const val TAG = "UserSearchRepository"
    }

    val searchQuery: Flow<String?> = dataStore.data.catch {
        if (it is IOException) {
            Log.i(TAG, "Error:", it)
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map { preferences ->
        preferences[SEARCH_QUERY_KEY]
    }

    suspend fun saveSearchQuery(query: String) {
        dataStore.edit { preferences ->
            preferences.clear()
            preferences[SEARCH_QUERY_KEY] = query
        }

        Log.i(TAG, "saveSearchQuery: ${dataStore.data.first()}")
    }
}