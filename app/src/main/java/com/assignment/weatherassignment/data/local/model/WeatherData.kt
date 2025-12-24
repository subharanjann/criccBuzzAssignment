package com.assignment.weatherassignment.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_forecast")
data class WeatherData(
    @PrimaryKey
    val date: String,
    val city: String,
    val temperature: Double,
    val condition: String,
    val icon: String,

    @ColumnInfo(name = "lastUpdated")
val lastUpdated: Long = System.currentTimeMillis()
)