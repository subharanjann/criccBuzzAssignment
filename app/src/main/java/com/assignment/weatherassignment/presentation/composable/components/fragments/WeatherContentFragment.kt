package com.assignment.weatherassignment.presentation.composable.components.fragments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.assignment.weatherassignment.data.local.model.WeatherData
import com.assignment.weatherassignment.util.Resource

class WeatherContentFragment {
}

@Composable
internal fun WeatherContent(
    resource: Resource<List<WeatherData>>?
) {
    when (resource) {
        is Resource.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is Resource.Success -> {
            resource.data?.let {
                ForecastList(it)
            }
        }

        is Resource.Error -> {
            Text(
                text = resource.message ?: "Something went wrong",
                color = MaterialTheme.colorScheme.error
            )
        }

        null -> Unit
    }
}
@Composable
fun ForecastList(data: List<WeatherData>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(data) { item ->
            ForecastCard(item)
        }
    }
}
@Composable
fun ForecastCard(item: WeatherData) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = item.date,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray
                )
                Text(
                    text = item.condition,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Text(
                text = "${item.temperature.toInt()}°C",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

