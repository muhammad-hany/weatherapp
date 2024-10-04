package com.mohammed.weatherapp.repository

import com.mohammed.weatherapp.data.datasource.local.LocalDataSource
import com.mohammed.weatherapp.data.datasource.remote.RemoteDataSource
import com.mohammed.weatherapp.models.DataState
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

    override suspend fun getWeather(location: String): DataState<WeatherResponse> {
        return remoteDataSource.getCurrentWeather(location)
    }

    override suspend fun getForecast(
        location: String,
        isCurrentLocation: Boolean
    ): DataState<ForecastResponse> {
        return if (isCurrentLocation && localDataSource.checkIfWeatherForecastExists()) {
            val cachedResponse = localDataSource.getWeatherForecast()
            if (cachedResponse is DataState.Success) {
                DataState.Success(cachedResponse.data, false)
            } else fetchRemoteForecast(location, true)
        } else fetchRemoteForecast(location, isCurrentLocation)
    }

    private suspend fun fetchRemoteForecast(
        location: String,
        isCurrentLocation: Boolean
    ): DataState<ForecastResponse> {
        val response = remoteDataSource.getForecast(query = location, days = 6)
        if (isCurrentLocation && response is DataState.Success) {
            localDataSource.saveWeatherForecast(response.data)
        }
        return response
    }

    override suspend fun search(query: String): DataState<List<WeatherSearchResponse>> {
        return remoteDataSource.search(query = query)
    }

    override suspend fun postQueryState(query: String) {
        _weatherQueryState.emit(query)
    }

    override fun getQueryState(): Flow<String?> = weatherQueryState
}