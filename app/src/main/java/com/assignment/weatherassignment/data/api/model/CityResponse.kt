package com.assignment.weatherassignment.data.api.model

class CityResponse : ArrayList<CityResponseItem>()

data class CityResponseItem(
    val country: String,
    val lat: Double,
    val lon: Double,
    val name: String,
    val state: String
)
