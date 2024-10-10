package com.example.flightsearchapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.flightsearchapp.data.repositories.AirportRepository
import com.example.flightsearchapp.data.repositories.AirportRepositoryImpl
import com.example.flightsearchapp.data.repositories.FavoriteRepositoryImpl
import com.example.flightsearchapp.data.repositories.FavoritesRepository
import com.example.flightsearchapp.data.repositories.UserSearchRepository

interface AppContainer {
    val airportRepository: AirportRepository
    val favoriteRepository: FavoritesRepository
    val userSearchRepository: UserSearchRepository

}



class DefaultAppContainer(context: Context, dataStore: DataStore<Preferences>) : AppContainer {

    override val airportRepository: AirportRepository =
        AirportRepositoryImpl(airportDao = FlightDatabase.getDatabase(context).airportDao)
    override val favoriteRepository: FavoritesRepository =
        FavoriteRepositoryImpl(favoriteDao = FlightDatabase.getDatabase(context).favoriteDao)
    override val userSearchRepository: UserSearchRepository =
        UserSearchRepository(dataStore = dataStore)
}
