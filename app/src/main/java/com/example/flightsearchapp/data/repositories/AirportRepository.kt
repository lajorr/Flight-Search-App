package com.example.flightsearchapp.data.repositories

import com.example.flightsearchapp.data.AirportDao
import com.example.flightsearchapp.models.Airport
import kotlinx.coroutines.flow.Flow


interface AirportRepository {

    fun getSearchedAirports(query: String): Flow<List<Airport>>
    fun getAllAirports(): Flow<List<Airport>>
}


class AirportRepositoryImpl(private var airportDao: AirportDao) : AirportRepository {
    override fun getSearchedAirports(query: String): Flow<List<Airport>> =
        airportDao.searchAirports("%$query%")

    override fun getAllAirports(): Flow<List<Airport>> = airportDao.getAllAirports()

}