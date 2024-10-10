package com.example.flightsearchapp.data.repositories

import com.example.flightsearchapp.data.FavoriteDao
import com.example.flightsearchapp.models.Favorite
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    fun getAllFavorites(): Flow<List<Favorite>>
    suspend fun addFavorite(favorite: Favorite)
    suspend fun removeFavorite(favorite: Favorite)
    suspend fun checkIfExists(departureCode: String, destinationCode: String): Int
}

class FavoriteRepositoryImpl(private val favoriteDao: FavoriteDao) : FavoritesRepository {
    override fun getAllFavorites(): Flow<List<Favorite>> = favoriteDao.getAllFavorites()

    override suspend fun addFavorite(favorite: Favorite) = favoriteDao.addFavorite(favorite)
    override suspend fun removeFavorite(favorite: Favorite) =
        favoriteDao.removeFavorite(favorite.departureCode, favorite.destinationCode)

    override suspend fun checkIfExists(departureCode: String, destinationCode: String): Int =
        favoriteDao.checkIfExists(departureCode, destinationCode)
}