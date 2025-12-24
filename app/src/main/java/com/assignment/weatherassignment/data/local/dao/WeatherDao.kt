package com.assignment.weatherassignment.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.assignment.weatherassignment.data.local.model.WeatherData

@Dao
interface WeatherDao {

    @Query(""" SELECT * FROM weather_forecast WHERE city = :city ORDER BY date ASC""")
    suspend fun getForecastForCity(city: String): List<WeatherData>

    @Query(""" SELECT * FROM weather_forecast ORDER BY lastUpdated DESC, date ASC""")
    suspend fun getLastForecast(): List<WeatherData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecast(items: List<WeatherData>)

    @Query("DELETE FROM weather_forecast WHERE city = :city")
    suspend fun clearCityForecast(city: String)
}
