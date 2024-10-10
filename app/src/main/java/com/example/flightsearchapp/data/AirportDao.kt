package com.example.flightsearchapp.data

import androidx.room.Dao
import androidx.room.Query
import com.example.flightsearchapp.models.Airport
import kotlinx.coroutines.flow.Flow


@Dao
interface AirportDao {

    @Query("SELECT * FROM airport WHERE iata_code LIKE :query OR name LIKE :query")
    fun searchAirports(query: String): Flow<List<Airport>>

    @Query("SELECT * FROM airport")
    fun getAllAirports(): Flow<List<Airport>>


}

