package com.assignment.weatherassignment.presentation.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.assignment.weatherassignment.domain.useCase.GetCachedWeatherUseCase
import com.assignment.weatherassignment.domain.useCase.GetWeatherByCityUseCase
import com.assignment.weatherassignment.domain.useCase.GetWeatherByLocationUseCase
import com.assignment.weatherassignment.domain.useCase.SearchCitiesUseCase
import com.assignment.weatherassignment.presentation.viewModel.WeatherViewModel


internal class WeatherViewModelFactory(
    private val searchCitiesUseCase: SearchCitiesUseCase,
    private val getWeatherByCityUseCase: GetWeatherByCityUseCase,
    private val getWeatherByLocationUseCase: GetWeatherByLocationUseCase,
    private val getCachedWeatherUseCase: GetCachedWeatherUseCase

) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            return WeatherViewModel(
                searchCitiesUseCase,
                getWeatherByCityUseCase,
                getWeatherByLocationUseCase,
                getCachedWeatherUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
