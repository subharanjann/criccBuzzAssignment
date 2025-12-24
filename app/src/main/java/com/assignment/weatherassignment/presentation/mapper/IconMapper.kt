package com.assignment.weatherassignment.presentation.mapper

import com.assignment.weatherassignment.R

class IconMapper {
}
fun getWeatherIconRes(
    condition: String,
    iconCode: String
): Int {
    val isNight = iconCode.endsWith("n")

    return when (condition) {
        "Clear" ->
            if (isNight) R.drawable.clear_night
            else R.drawable.clear_day

        "Clouds" ->
            R.drawable.cloudy

        "Rain", "Drizzle" ->
            R.drawable.rain

        "Thunderstorm" ->
            R.drawable.rain

        "Snow" ->
            R.drawable.snow

        "Mist", "Fog", "Haze", "Smoke" ->
            R.drawable.fog

        "Wind" ->
            R.drawable.wind

        else ->
            R.drawable.cloudy
    }
}
