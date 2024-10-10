package com.example.flightsearchapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.flightsearchapp.models.Airport
import com.example.flightsearchapp.models.Favorite

@Database(entities = [Airport::class, Favorite::class], version = 1)
abstract class FlightDatabase : RoomDatabase() {

    abstract val airportDao: AirportDao
    abstract val favoriteDao: FavoriteDao

    companion object {
        @Volatile
        private var instance: FlightDatabase? = null

        fun getDatabase(context: Context): FlightDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context, FlightDatabase::class.java, "flight_database"
                ).createFromAsset("database/flight_search.db")
                    .build()
                    .also { instance = it }
            }
        }
    }
}


