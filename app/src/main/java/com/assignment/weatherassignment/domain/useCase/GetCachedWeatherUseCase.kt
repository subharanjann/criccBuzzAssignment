package com.assignment.weatherassignment.domain.useCase

import com.assignment.weatherassignment.data.local.model.WeatherData
import com.assignment.weatherassignment.domain.repository.WeatherRepository
import com.assignment.weatherassignment.util.Resource


internal class GetCachedWeatherUseCase(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(): Resource<List<WeatherData>> {
        return repository.getCachedForecast()
    }
}
