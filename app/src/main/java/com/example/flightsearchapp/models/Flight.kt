package com.example.flightsearchapp.models

data class Flight(
    val origin: Airport,
    val destination: Airport,
    var isFavorite: Boolean = false
) {

    fun toggleFavorite(): Flight {
//        this.isFavorite = !this.isFavorite
        return this.copy(isFavorite = !isFavorite)
    }

    fun toFavorite(): Favorite {
        return Favorite(
            departureCode = origin.iataCode,
            destinationCode = destination.iataCode
        )
    }

}
