package com.assignment.weatherassignment.presentation.composable.components.fragments

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.assignment.weatherassignment.data.api.model.CityResponseItem
import com.assignment.weatherassignment.util.Resource

class CitySearchResult {
}

@Composable
internal fun CitySearchResult(
    resource: Resource<List<CityResponseItem>>?,
    onCitySelected: (CityResponseItem) -> Unit
) {
    when (resource) {
        is Resource.Loading -> {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }

        is Resource.Success -> {
            resource.data?.takeIf { it.isNotEmpty() }?.let { cities ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column {
                        cities.forEach { city ->
                            Text(
                                text = "${city.name}, ${city.country}",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onCitySelected(city) }
                                    .padding(12.dp)
                            )
                        }
                    }
                }
            }
        }

        is Resource.Error -> {
            Text(
                text = resource.message ?: "",
                color = MaterialTheme.colorScheme.error
            )
        }

        null -> Unit
    }
}
