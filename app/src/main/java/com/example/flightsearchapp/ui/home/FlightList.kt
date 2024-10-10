package com.example.flightsearchapp.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flightsearchapp.R
import com.example.flightsearchapp.models.Flight

@Composable
fun FlightList(
    originIataCode: String? = null, flightList: List<Flight>, onFavoriteClick: (Flight) -> Unit
) {
    Column {
        if (originIataCode != null) Text(
            stringResource(R.string.flights_from, originIataCode),
            fontWeight = FontWeight.Bold,
        ) else {
            Text(
                stringResource(R.string.favorite_flights),
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        if (flightList.isEmpty()) Text(
            stringResource(R.string.no_favorites_yet),
            modifier = Modifier.fillMaxSize(),
            textAlign = TextAlign.Center
        )
        else LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(flightList) { flight ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(topEnd = 20.dp))
                        .background(
                            Color(0xffe0e2ec)
                        )
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                stringResource(R.string.depart),
                                fontSize = 12.sp,
                                color = Color.DarkGray
                            )
                            Row {
                                Text(
                                    text = flight.origin.iataCode,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = flight.origin.name,
                                    fontSize = 14.sp,
                                    color = Color.DarkGray
                                )
                            }
                            Text(
                                stringResource(R.string.arrive),
                                fontSize = 12.sp,
                                color = Color.DarkGray
                            )
                            Row() {
                                Text(
                                    text = flight.destination.iataCode,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = flight.destination.name,
                                    fontSize = 14.sp,
                                    color = Color.DarkGray
                                )
                            }
                        }

                        IconButton(onClick = {

                            onFavoriteClick(flight)
                        }) {
                            Icon(
                                Icons.Filled.Star,
                                modifier = Modifier.size(32.dp),
                                contentDescription = null,
                                tint = if (flight.isFavorite) Color.Yellow else Color.Gray,
                            )
                        }
                    }
                }
            }
        }
    }
}