package com.assignment.weatherassignment.domain.useCase

import com.assignment.weatherassignment.data.local.model.WeatherData
import com.assignment.weatherassignment.domain.repository.WeatherRepository
import com.assignment.weatherassignment.util.Resource

internal class GetWeatherByLocationUseCase(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(
        lat: Double,
        lon: Double
    ): Resource<List<WeatherData>> {
        return repository.getForecastByLocation(lat, lon)
    }
}
