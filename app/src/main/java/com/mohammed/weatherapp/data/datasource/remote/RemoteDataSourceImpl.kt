package com.mohammed.weatherapp.data.datasource.remote

import com.mohammed.weatherapp.data.api.WeatherApi
import com.mohammed.weatherapp.models.DataState
import com.mohammed.weatherapp.models.ForecastResponse
import com.mohammed.weatherapp.models.WeatherResponse
import com.mohammed.weatherapp.models.WeatherSearchResponse

class RemoteDataSourceImpl(
    private val weatherApi: WeatherApi
) : RemoteDataSource {

    override suspend fun getForecast(query: String, days: Int): DataState<ForecastResponse> {
        return try {
            val response = weatherApi.getForecast(query = query, days = days)
            DataState.Success(response, true)
        } catch (e: Exception) {
            DataState.Error("error happened while fetching forecast", e, true)
        }
    }

    override suspend fun getCurrentWeather(query: String): DataState<WeatherResponse> {
        return try {
            val response = weatherApi.getCurrentWeather(query = query)
            DataState.Success(response, true)
        } catch (e: Exception) {
            DataState.Error("error happened while fetching current weather", e, true)
        }
    }

    override suspend fun search(query: String): DataState<List<WeatherSearchResponse>> {
        return try {
            val response = weatherApi.search(query = query)
            DataState.Success(response, true)
        } catch (e: Exception) {
            DataState.Error("error happened while searching", e, true)
        }
    }
}