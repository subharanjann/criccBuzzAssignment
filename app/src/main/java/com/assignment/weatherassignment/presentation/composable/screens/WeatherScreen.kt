package com.assignment.weatherassignment.presentation.composable.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.assignment.weatherassignment.presentation.composable.components.CitySuggestionPanel
import com.assignment.weatherassignment.presentation.composable.components.CurrentWeatherShow
import com.assignment.weatherassignment.presentation.composable.components.ForecastBottomBar
import com.assignment.weatherassignment.presentation.composable.components.OfflineNetworkIndicator
import com.assignment.weatherassignment.presentation.composable.components.TopSearchBar
import com.assignment.weatherassignment.presentation.theme.DarkBg
import com.assignment.weatherassignment.presentation.theme.DarkBgSecondary
import com.assignment.weatherassignment.presentation.theme.NeonPurple
import com.assignment.weatherassignment.presentation.viewModel.WeatherViewModel
import com.assignment.weatherassignment.util.NetworkMonitor
import com.assignment.weatherassignment.util.Resource

class WeatherScreen {
}


@Composable
internal fun WeatherScreenBody(
    viewModel: WeatherViewModel,
    networkMonitor: NetworkMonitor,
    onRefreshClick : () -> Unit
) {
    val weatherResult by viewModel.weatherResult.observeAsState()
    val citySearchResult by viewModel.citySearchResult.observeAsState()
    val isOnline by viewModel.isOnline.observeAsState(true)
    var isSearchExpanded by rememberSaveable { mutableStateOf(false) }
    var searchText by rememberSaveable { mutableStateOf("") }

    NeoBackground {

        Box(modifier = Modifier.fillMaxSize()) {

            // MAIN CONTENT
            when (weatherResult) {
                is Resource.Success -> {
                    val data = weatherResult!!.data!!
                    CurrentWeatherShow(
                        modifier = Modifier.align(Alignment.Center),
                        weather = data.first(),
                        isOnline
                    )

                    ForecastBottomBar(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        forecast = data
                    )
                }

                is Resource.Loading -> CenterLoading()
                is Resource.Error -> CenterError(weatherResult!!.message)
                null -> Unit
            }

            // TOP SEARCH
            Column(modifier = Modifier.align(Alignment.TopCenter)) {
                Spacer(Modifier.height(40.dp))
                TopSearchBar(
                    isExpanded = isSearchExpanded,
                    searchText = searchText,
                    onSearchTextChanged = {
                        searchText = it
                        viewModel.onCityQueryChanged(it)
                    },
                    onExpand = { isSearchExpanded = true },
                    onCollapse = {
                        isSearchExpanded = false
                        searchText = ""
                    }
                )

                Row(
                    Modifier.fillMaxWidth(1f),
                    horizontalArrangement = Arrangement.End,){
                    OfflineNetworkIndicator(
                        isOnline = isOnline,
                        onRetry = {
                            val online = networkMonitor.isOnline()
                            viewModel.updateNetworkStatus(online)

                            if (online) {
                                onRefreshClick()
                            }
                        }
                    )
                    Spacer(Modifier.height(1.dp).width(12.dp))

                }

                CitySuggestionPanel(
                    resource = citySearchResult,
                    onCitySelected = { city ->
                        viewModel.fetchWeatherByCity(city.name)
                        isSearchExpanded = false
                        searchText = ""
                    }
                )
            }
        }
    }
}


@Composable
fun NeoBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        DarkBg,
                        DarkBgSecondary
                    )
                )
            )
    ) {
        content()
    }
}






@Composable
fun CenterLoading() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = NeonPurple)
    }
}

@Composable
fun CenterError(message: String?) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = message ?: "Something went wrong",
            color = MaterialTheme.colorScheme.error
        )
    }
}
