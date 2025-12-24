package com.assignment.weatherassignment.data.repository

import android.util.Log
import com.assignment.weatherassignment.data.api.apiClient.ApiServiceInterface
import com.assignment.weatherassignment.data.api.model.CityResponseItem
import com.assignment.weatherassignment.data.local.dao.WeatherDao
import com.assignment.weatherassignment.data.local.model.WeatherData
import com.assignment.weatherassignment.domain.repository.WeatherRepository
import com.assignment.weatherassignment.domain.mapper.toThreeDayForecast
import com.assignment.weatherassignment.util.Resource

internal class WeatherRepositoryImplementation(
    private val api: ApiServiceInterface,
    private val dao: WeatherDao,
    private val apiKey: String
) : WeatherRepository {

    override suspend fun searchCities(
        query: String
    ): Resource<List<CityResponseItem>> {
        return try {
            val response = api.searchCities(
                query = query,
                apiKey = apiKey
            )

            if (response.isSuccessful) {
                response.body()?.let { cities ->
                    Resource.Success(cities)
                } ?: Resource.Error("City list is empty")
            } else {
                val errorMsg = response.errorBody()?.string()
                Resource.Error(errorMsg ?: "City search failed")
            }

        } catch (e: Exception) {
            Log.e("WeatherRepository", e.localizedMessage ?: "Unknown error")
            Resource.Error(e.localizedMessage ?: "Something went wrong")
        }
    }



    override suspend fun getForecastByCity(
        cityName: String
    ): Resource<List<WeatherData>> {
        return try {
            val response = api.getForecast(
                city = cityName,
                apiKey = apiKey
            )

            if (response.isSuccessful) {
                response.body()?.let { body ->

                    val weatherData = body.toThreeDayForecast()

                    dao.clearCityForecast(body.city.name)
                    dao.insertForecast(weatherData)

                    Resource.Success(weatherData)

                } ?: Resource.Error("Weather response is empty")

            } else {
                fallbackToCache(cityName, response.errorBody()?.string())
            }

        } catch (e: Exception) {
            fallbackToCache(cityName, e.localizedMessage)
        }
    }



    override suspend fun getForecastByLocation(
        lat: Double,
        lon: Double
    ): Resource<List<WeatherData>> {
        return try {
            val response = api.getForecast(
                lat = lat,
                lon = lon,
                apiKey = apiKey
            )

            if (response.isSuccessful) {
                response.body()?.let { body ->

                    val weatherData = body.toThreeDayForecast()

                    dao.clearCityForecast(body.city.name)
                    dao.insertForecast(weatherData)

                    Resource.Success(weatherData)

                } ?: Resource.Error("Weather response is empty")

            } else {
                val errorMsg = response.errorBody()?.string()
                getCachedWeather(null, errorMsg)
            }

        } catch (e: Exception) {
            Log.e("WeatherRepository", e.localizedMessage ?: "Network error")
            getCachedWeather(null, e.localizedMessage)
        }
    }

    private suspend fun getCachedWeather(
        city: String?,
        errorMessage: String?
    ): Resource<List<WeatherData>> {

        val cachedData = city?.let {
            dao.getForecastForCity(it)
        }.orEmpty()

        return if (cachedData.isNotEmpty()) {
            Resource.Success(cachedData)
        } else {
            Resource.Error(errorMessage ?: "No offline data available")
        }
    }

    override suspend fun getCachedForecast(): Resource<List<WeatherData>> {
        val cached = dao.getLastForecast()
        return if (cached.isNotEmpty()) {
            Resource.Success(cached)
        } else {
            Resource.Error("No offline data available")
        }
    }


    private suspend fun fallbackToCache(
        city: String?,
        errorMessage: String?
    ): Resource<List<WeatherData>> {

        val cached = when {
            !city.isNullOrEmpty() -> dao.getForecastForCity(city)
            else -> dao.getLastForecast()
        }

        return if (cached.isNotEmpty()) {
            Resource.Success(cached)
        } else {
            Resource.Error(errorMessage ?: "No offline data available")
        }
    }

}
