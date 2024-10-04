package com.mohammed.weatherapp.data.api

import com.mohammed.weatherapp.BuildConfig
import com.mohammed.weatherapp.models.ForecastResponse
import com.mohammed.weatherapp.models.WeatherResponse
import com.mohammed.weatherapp.models.WeatherSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("forecast.json")
    suspend fun getForecast(
        @Query("q") query: String,
        @Query("days") days: Int = 6
    ): ForecastResponse

    @GET("current.json")
    suspend fun getCurrentWeather(@Query("q") query: String): WeatherResponse

    @GET("search.json")
    suspend fun search(@Query("q") query: String): List<WeatherSearchResponse>
}