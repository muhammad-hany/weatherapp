package com.mohammed.weatherapp.data.datasource.remote

import com.mohammed.weatherapp.data.api.WeatherApi
import com.mohammed.weatherapp.models.ForecastResponse
import com.mohammed.weatherapp.models.WeatherResponse
import com.mohammed.weatherapp.models.WeatherSearchResponse

class RemoteDataSourceImpl(
    private val weatherApi: WeatherApi
) : RemoteDataSource {

    override suspend fun getForecast(query: String, days: Int): ForecastResponse {
        return weatherApi.getForecast(query = query, days = days)
    }

    override suspend fun getCurrentWeather(query: String): WeatherResponse {
        return weatherApi.getCurrentWeather(query = query)
    }

    override suspend fun search(query: String): List<WeatherSearchResponse> {
        return weatherApi.search(query = query)
    }
}