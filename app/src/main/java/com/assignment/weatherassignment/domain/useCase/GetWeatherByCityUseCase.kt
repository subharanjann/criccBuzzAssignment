package com.assignment.weatherassignment.domain.useCase

import com.assignment.weatherassignment.data.local.model.WeatherData
import com.assignment.weatherassignment.domain.repository.WeatherRepository
import com.assignment.weatherassignment.util.Resource


internal class GetWeatherByCityUseCase(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(
        cityName: String
    ): Resource<List<WeatherData>> {
        return repository.getForecastByCity(cityName)
    }
}

