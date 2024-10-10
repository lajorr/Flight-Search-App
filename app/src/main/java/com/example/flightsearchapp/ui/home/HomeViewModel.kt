package com.example.flightsearchapp.ui.home

import android.util.Log
import androidx.compose.material3.TextField
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flightsearchapp.FlightSearchApplication
import com.example.flightsearchapp.data.repositories.AirportRepository
import com.example.flightsearchapp.data.repositories.FavoritesRepository
import com.example.flightsearchapp.data.repositories.UserSearchRepository
import com.example.flightsearchapp.models.Airport
import com.example.flightsearchapp.models.Flight
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.w3c.dom.Text


private const val TAG = "home_vm"

class HomeViewModel(
    private val airportRepository: AirportRepository,
    private val favoritesRepository: FavoritesRepository,
    private val userSearchRepository: UserSearchRepository
) : ViewModel() {

    private lateinit var _allAirports: List<Airport>


    private var _textState = MutableStateFlow("")

    val textState: StateFlow<TextFieldValue> = _textState.map {
        TextFieldValue(
            text = it, selection = TextRange(it.length)
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TextFieldValue(text = "", selection = TextRange(0))
    )


    var recommendationState = MutableStateFlow(emptyList<Airport>())
        private set

    private val _flightListState = MutableStateFlow<List<Flight>>(mutableListOf())

    private val _visibilityState = MutableStateFlow(false)


    var flightState = combine(_flightListState, _visibilityState) { flights, visibility ->
        FlightUiState(flights = flights, visibility = visibility)
    }.map { FlightUiState(flights = it.flights, visibility = it.visibility) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = FlightUiState()
    )

    var favoriteFlightState: StateFlow<List<Flight>> = favoritesRepository.getAllFavorites().map {
        it.mapNotNull { favorite ->
            val originCode = favorite.departureCode
            val destinationCode = favorite.destinationCode

            val originAirport = _allAirports.find { airport -> airport.iataCode == originCode }
            val destinationAirport =
                _allAirports.find { airport -> airport.iataCode == destinationCode }

            if (originAirport != null && destinationAirport != null) {
                Flight(
                    origin = originAirport, destination = destinationAirport, isFavorite = true
                )
            } else {
                null
            }
        }
    }.stateIn(
        scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000), mutableListOf()
    )


    init {
        viewModelScope.launch(Dispatchers.IO) {
            _allAirports = airportRepository.getAllAirports().first()

            userSearchRepository.searchQuery.collect {
                if (it != null) {
                    _textState.value = it
                    getRecommendation()
                }
            }
        }
    }


    fun onAirportSelected(airport: Airport) {
        // recommendation
        val airportsDestination = _allAirports.filter { it.iataCode != airport.iataCode }

        val flightList = airportsDestination.map {
            Flight(origin = airport, destination = it)
        }


        // setting favorites for all flights in the list
        viewModelScope.launch {
            for (flight in flightList) {
                val f = flight.toFavorite()
                if (favoritesRepository.checkIfExists(
                        f.departureCode, f.destinationCode
                    ) == 1
                ) {
                    flight.toggleFavorite()

                }
            }

            val tempList: List<Flight> = flightList.map { flight ->
                val f = flight.toFavorite()
                if (favoritesRepository.checkIfExists(
                        f.departureCode, f.destinationCode
                    ) == 1
                ) {
                    flight.toggleFavorite()
                } else flight
            }
            _flightListState.value = tempList
            _visibilityState.value = !_visibilityState.value
        }
    }

    suspend fun setFavorite(flight: Flight) {
        val f = flight.toFavorite()
        if (favoritesRepository.checkIfExists(f.departureCode, f.destinationCode) == 1) {
            favoritesRepository.removeFavorite(flight.toFavorite())

        } else {
            favoritesRepository.addFavorite(flight.toFavorite())
        }

        _flightListState.value = _flightListState.value.map {
            if (it.origin == flight.origin && it.destination == flight.destination) {
                it.toggleFavorite()
            } else {
                it
            }
        }
    }


    fun onTextChange(newText: TextFieldValue) {
        _textState.value = newText.text
        viewModelScope.launch {
            userSearchRepository.saveSearchQuery(newText.text)
            Log.i(TAG, "onTextChange: ${_textState.value}")
            if (_textState.value.trim().isEmpty()) {
                _visibilityState.value = false
            } else {
                getRecommendation()

            }
        }
    }

    private fun getRecommendation() {
        if (_textState.value.trim().isNotEmpty()) {
            val flow = airportRepository.getSearchedAirports(_textState.value.trim())
            viewModelScope.launch {
                flow.collect {
                    recommendationState.value = it
                }
            }
        } else {
            recommendationState.value = mutableListOf()
        }

    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as FlightSearchApplication
                HomeViewModel(
                    application.container.airportRepository,
                    application.container.favoriteRepository,
                    application.container.userSearchRepository
                )


            }
        }
    }
}

data class FlightUiState(
    val flights: List<Flight> = emptyList(), val visibility: Boolean = false
)

