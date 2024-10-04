package com.mohammed.weatherapp.data.datasource.local

import com.mohammed.weatherapp.models.ForecastResponse

interface LocalDataSource {
    suspend fun saveWeatherForecast(weatherForecast: ForecastResponse)
    suspend fun checkIfWeatherForecastExists(): Boolean
    suspend fun getWeatherForecast(): ForecastResponse?
}