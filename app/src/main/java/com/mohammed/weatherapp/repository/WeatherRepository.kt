package com.mohammed.weatherapp.repository

import com.mohammed.weatherapp.models.ForecastResponse
import com.mohammed.weatherapp.models.WeatherResponse
import com.mohammed.weatherapp.models.WeatherSearchResponse
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getWeather(location: String): WeatherResponse
    suspend fun getForecast(location: String, isCurrentLocation: Boolean): ForecastResponse
    suspend fun search(query: String): List<WeatherSearchResponse>
    suspend fun postQueryState(query: String)
    fun getQueryState(): Flow<String?>
}