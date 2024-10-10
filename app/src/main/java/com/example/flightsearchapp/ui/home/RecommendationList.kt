package com.example.flightsearchapp.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flightsearchapp.models.Airport

@Composable
fun RecommendationList(
    airportRecommendations: List<Airport>, onRecommendationSelected: (Airport) -> Unit
) {
    LazyColumn {
        items(airportRecommendations) { airport ->
            Row(modifier = Modifier.clickable {
                onRecommendationSelected(airport)

            }) {
                Text(
                    text = airport.iataCode, fontWeight = FontWeight.Bold, fontSize = 14.sp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = airport.name, fontSize = 14.sp, color = Color.DarkGray
                )
            }
        }
    }
}