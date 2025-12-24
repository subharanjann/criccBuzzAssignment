package com.assignment.weatherassignment.domain.mapper

import com.assignment.weatherassignment.data.api.model.WeatherResponse
import com.assignment.weatherassignment.data.local.model.WeatherData

class WeatherMapper {
}

fun WeatherResponse.toThreeDayForecast(): List<WeatherData> {

    return list
        .groupBy { it.dateTime.substring(0, 10) }
        .values
        .take(3)
        .map { dayItems ->

            val noonItem = dayItems.minByOrNull { item ->
                val hour = item.dateTime.substring(11, 13).toInt()
                kotlin.math.abs(hour - 12)
            } ?: dayItems.first()

            WeatherData(
                date = noonItem.dateTime.substring(0, 10),
                city = city.name,
                temperature = noonItem.main.temp,
                condition = noonItem.weather.first().main,
                icon = noonItem.weather.first().icon
            )
        }
}
