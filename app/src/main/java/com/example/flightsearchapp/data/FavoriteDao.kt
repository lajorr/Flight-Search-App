package com.example.flightsearchapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.flightsearchapp.models.Favorite
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorite")
    fun getAllFavorites(): Flow<List<Favorite>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavorite(favorite: Favorite)

    @Query("DELETE FROM favorite WHERE departure_code = :departureCode AND destination_code = :destinationCode")
    suspend fun removeFavorite(departureCode: String, destinationCode: String)

    @Query("SELECT EXISTS ( SELECT 1 FROM favorite WHERE departure_code = :departureCode AND destination_code = :destinationCode) ")
    suspend fun checkIfExists(departureCode: String, destinationCode: String): Int

}