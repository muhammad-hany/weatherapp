package com.mohammed.weatherapp.data.datasource.remote

import com.mohammed.weatherapp.models.ForecastResponse
import com.mohammed.weatherapp.models.WeatherResponse
import com.mohammed.weatherapp.models.WeatherSearchResponse

interface RemoteDataSource {
    suspend fun getForecast(query: String, days: Int): ForecastResponse
    suspend fun getCurrentWeather(query: String): WeatherResponse
    suspend fun search(query: String): List<WeatherSearchResponse>
}