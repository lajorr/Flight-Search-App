package com.example.flightsearchapp.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearchapp.R
import com.example.flightsearchapp.ui.common.Appbar
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
) {
    val searchTextState by viewModel.textState.collectAsState()


    val recommendations by viewModel.recommendationState.collectAsState()
    val flightState by viewModel.flightState.collectAsState()
    val favoriteState by viewModel.favoriteFlightState.collectAsState()
    val coroutineScope = rememberCoroutineScope()


    Scaffold(modifier = modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background),
        topBar = { Appbar(title = stringResource(R.string.flight_search)) }) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .padding(
                    12.dp
                )

        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50.dp))
                    .background(
                        color = Color(0xffd5e3ff)
                    ),
                value = searchTextState,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                onValueChange = viewModel::onTextChange,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "searchIcon") },
                trailingIcon =
                {
                    if (searchTextState.text.isNotEmpty())
                        IconButton(onClick = {
                            viewModel.onTextChange(searchTextState.copy(text = ""))
                        }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "clear"
                            )
                        }

                },
                placeholder = { Text(text = "Search") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                ),
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (searchTextState.text.trim().isNotEmpty())
            //recommendations
                if (!flightState.visibility) RecommendationList(
                    airportRecommendations = recommendations,
                    onRecommendationSelected = viewModel::onAirportSelected
                )
                else FlightList(flightList = flightState.flights,
                    originIataCode = flightState.flights[0].origin.iataCode,
                    onFavoriteClick = {
                        coroutineScope.launch {
                            viewModel.setFavorite(it)
                        }
                    })
            else FlightList(flightList = favoriteState, onFavoriteClick = {
                coroutineScope.launch {
                    viewModel.setFavorite(it)
                }

            })
        }


    }
}


@Preview
@Composable
private fun HomeScreenPrev() {
    HomeScreen()

}


