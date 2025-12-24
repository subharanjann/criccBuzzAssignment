package com.assignment.weatherassignment.data.api.apiClient


import com.assignment.weatherassignment.data.api.model.CityResponse
import com.assignment.weatherassignment.data.api.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiServiceInterface {

     @GET("data/2.5/forecast")
     suspend fun getForecast(
         @Query("q") city: String? = null,
         @Query("lat") lat: Double? = null,
         @Query("lon") lon: Double? = null,
         @Query("units") units: String = "metric",
         @Query("appid") apiKey: String
     ): Response<WeatherResponse>

     @GET("geo/1.0/direct")
     suspend fun searchCities(
         @Query("q") query: String,
         @Query("limit") limit: Int = 5,
         @Query("appid") apiKey: String
     ): Response<CityResponse>
}