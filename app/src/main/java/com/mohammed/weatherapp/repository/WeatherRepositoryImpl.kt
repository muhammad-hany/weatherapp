package com.mohammed.weatherapp.repository

import com.mohammed.weatherapp.data.datasource.local.LocalDataSource
import com.mohammed.weatherapp.data.datasource.remote.RemoteDataSource
import com.mohammed.weatherapp.models.ForecastResponse
import com.mohammed.weatherapp.models.WeatherResponse
import com.mohammed.weatherapp.models.WeatherSearchResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class WeatherRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : WeatherRepository {

    private val _weatherQueryState = MutableStateFlow<String?>(null)
    private val weatherQueryState: Flow<String?> get() = _weatherQueryState

    override suspend fun getWeather(location: String): WeatherResponse {
        return remoteDataSource.getCurrentWeather(location)
    }

    override suspend fun getForecast(
        location: String,
        isCurrentLocation: Boolean
    ): ForecastResponse {
        return if (isCurrentLocation && localDataSource.checkIfWeatherForecastExists()) {
            localDataSource.getWeatherForecast() ?: fetchRemoteForecast(location, true)
        } else fetchRemoteForecast(location, isCurrentLocation)
    }

    private suspend fun fetchRemoteForecast(
        location: String,
        isCurrentLocation: Boolean
    ): ForecastResponse {
        val response = remoteDataSource.getForecast(query = location, days = 6)
        if (isCurrentLocation) {
            localDataSource.saveWeatherForecast(response)
        }
        return response
    }

    override suspend fun search(query: String): List<WeatherSearchResponse> {
        return remoteDataSource.search(query = query)
    }

    override suspend fun postQueryState(query: String) {
        _weatherQueryState.emit(query)
    }

    override fun getQueryState(): Flow<String?> = weatherQueryState
}