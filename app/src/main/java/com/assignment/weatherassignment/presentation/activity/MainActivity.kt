package com.assignment.weatherassignment.presentation.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.assignment.weatherassignment.BuildConfig
import com.assignment.weatherassignment.data.api.apiClient.ApiClient
import com.assignment.weatherassignment.data.repository.WeatherRepositoryImplementation
import com.assignment.weatherassignment.data.local.db.WeatherDatabase
import com.assignment.weatherassignment.domain.repository.WeatherRepository
import com.assignment.weatherassignment.domain.useCase.GetCachedWeatherUseCase
import com.assignment.weatherassignment.domain.useCase.GetWeatherByCityUseCase
import com.assignment.weatherassignment.domain.useCase.GetWeatherByLocationUseCase
import com.assignment.weatherassignment.domain.useCase.SearchCitiesUseCase
import com.assignment.weatherassignment.presentation.composable.screens.WeatherScreenBody
import com.assignment.weatherassignment.presentation.theme.WeatherAssignmentTheme
import com.assignment.weatherassignment.presentation.viewModel.WeatherViewModel
import com.assignment.weatherassignment.presentation.viewModelFactory.WeatherViewModelFactory
import com.assignment.weatherassignment.util.NetworkMonitor
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: WeatherViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var networkMonitor: NetworkMonitor



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setUp()
        setContent {
            WeatherAssignmentTheme {
                WeatherScreenBody(viewModel,networkMonitor){
                    requestLocationAndFetchWeather()
                }
            }
        }
    }

    private fun setUp() {

        vmSetter()

        networkMonitor = NetworkMonitor(this)
        viewModel.updateNetworkStatus(networkMonitor.isOnline())

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        requestLocationAndFetchWeather()
    }


    private fun vmSetter() {
        val database = WeatherDatabase.Companion.getInstance(applicationContext)
        val repository: WeatherRepository =
            WeatherRepositoryImplementation(
                api = ApiClient.getService(),
                dao = database.weatherDao(),
                apiKey = BuildConfig.OPEN_WEATHER_API_KEY
            )

        val searchCitiesUseCase = SearchCitiesUseCase(repository)
        val getWeatherByCityUseCase = GetWeatherByCityUseCase(repository)
        val getWeatherByLocationUseCase = GetWeatherByLocationUseCase(repository)
        val getCachedWeatherUseCase = GetCachedWeatherUseCase(repository)

        val factory = WeatherViewModelFactory(
            searchCitiesUseCase,
            getWeatherByCityUseCase,
            getWeatherByLocationUseCase,
            getCachedWeatherUseCase
        )

        viewModel = ViewModelProvider(this, factory)[WeatherViewModel::class.java]

    }
    private fun requestLocationAndFetchWeather() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fetchLastKnownLocation()
        } else {
            locationPermissionLauncher.launch(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }
    @SuppressLint("MissingPermission")
    private fun fetchLastKnownLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                location?.let {
                    viewModel.fetchWeatherByLocation(
                        lat = it.latitude,
                        lon = it.longitude
                    )
                }
            }
    }

    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                fetchLastKnownLocation()
            } else {

            }
        }


}