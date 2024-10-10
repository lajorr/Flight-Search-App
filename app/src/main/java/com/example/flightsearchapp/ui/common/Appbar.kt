package com.example.flightsearchapp.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.flightsearchapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Appbar(modifier: Modifier = Modifier, title: String) {
    TopAppBar(
        modifier = modifier.fillMaxWidth(), title = {
            Text(
                text = title,
                color = Color.White
            )
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xff205fa6)
        )
    )
}


@Preview
@Composable
private fun AppbarPreview() {

    Appbar(title = stringResource(R.string.flight_search))

}