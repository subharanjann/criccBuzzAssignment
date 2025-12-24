package com.assignment.weatherassignment.data.api.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val city: CitiesResponse,
    val list: List<WeatherItemResponse>
)

data class CitiesResponse(
    val name: String
)

data class WeatherItemResponse(
    @SerializedName("dt_txt")
    val dateTime: String,
    val main: MainResponse,
    val weather: List<WeatherConditionResponse>
)

data class MainResponse(
    val temp: Double
)

data class WeatherConditionResponse(
    val main: String,
    val description: String,
    val icon: String
)
