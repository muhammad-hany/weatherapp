package com.mohammed.weatherapp.data.datasource.remote

import com.mohammed.weatherapp.models.DataState
import com.mohammed.weatherapp.models.ForecastResponse
import com.mohammed.weatherapp.models.WeatherResponse
import com.mohammed.weatherapp.models.WeatherSearchResponse

interface RemoteDataSource {
    suspend fun getForecast(query: String, days: Int): DataState<ForecastResponse>
    suspend fun getCurrentWeather(query: String): DataState<WeatherResponse>
    suspend fun search(query: String): DataState<List<WeatherSearchResponse>>
}