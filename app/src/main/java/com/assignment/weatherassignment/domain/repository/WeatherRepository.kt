package com.assignment.weatherassignment.domain.repository

import com.assignment.weatherassignment.data.api.model.CityResponseItem
import com.assignment.weatherassignment.data.local.model.WeatherData
import com.assignment.weatherassignment.util.Resource

internal interface WeatherRepository {

    suspend fun searchCities(
        query: String
    ): Resource<List<CityResponseItem>>

    suspend fun getForecastByCity(
        cityName: String
    ): Resource<List<WeatherData>>

    suspend fun getForecastByLocation(
        lat: Double,
        lon: Double
    ): Resource<List<WeatherData>>

    suspend fun getCachedForecast(): Resource<List<WeatherData>>

}