package com.mohammed.weatherapp.repository

import com.mohammed.weatherapp.models.DataState
import com.mohammed.weatherapp.models.ForecastResponse
import com.mohammed.weatherapp.models.WeatherResponse
import com.mohammed.weatherapp.models.WeatherSearchResponse
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getWeather(location: String): DataState<WeatherResponse>
    suspend fun getForecast(location: String, isCurrentLocation: Boolean): DataState<ForecastResponse>
    suspend fun search(query: String): DataState<List<WeatherSearchResponse>>
    suspend fun postQueryState(query: String)
    fun getQueryState(): Flow<String?>
}