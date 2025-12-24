package com.assignment.weatherassignment.presentation.composable.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.assignment.weatherassignment.data.local.model.WeatherData
import com.assignment.weatherassignment.presentation.mapper.getWeatherIconRes
import com.assignment.weatherassignment.presentation.theme.DarkBg
import com.assignment.weatherassignment.presentation.theme.DarkBgSecondary
import com.assignment.weatherassignment.presentation.theme.NeonPurpleSoft
import com.assignment.weatherassignment.presentation.theme.TextPrimary
import com.assignment.weatherassignment.presentation.theme.TextSecondary
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class ThreeDayForcast {
}

@Composable
fun ForecastBottomBar(
    modifier: Modifier = Modifier,
    forecast: List<WeatherData>
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = DarkBgSecondary.copy(alpha = 0.95f),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            )
            .padding(vertical = 16.dp)
    ) {

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                space = 12.dp,
                alignment = Alignment.CenterHorizontally
            ),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ){
            items(forecast) { item ->
                MiniForecastCard(item)
            }
        }
    }
}

@Composable
fun MiniForecastCard(item: WeatherData) {

    val iconRes = getWeatherIconRes(
        condition = item.condition,
        iconCode = item.icon
    )

    Column(
        modifier = Modifier
            .width(90.dp)
            .background(
                color = DarkBg.copy(alpha = 0.9f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = formatDayAndDate(item.date), // Wed, 24
            color = TextSecondary,
            style = MaterialTheme.typography.labelSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Icon(
            painter = painterResource(iconRes),
            contentDescription = item.condition,
            modifier = Modifier.size(24.dp),
            tint = NeonPurpleSoft
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "${item.temperature.toInt()}°",
            color = TextPrimary,
            style = MaterialTheme.typography.titleMedium
        )
    }
}


fun formatDayAndDate(date: String): String {
    val localDate = LocalDate.parse(date)
    val formatter = DateTimeFormatter.ofPattern("EEE, dd", Locale.getDefault())
    return localDate.format(formatter)
}