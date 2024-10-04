package com.mohammed.weatherapp.models

import com.squareup.moshi.Json

data class ForecastResponse (
    @Json(name = "location") val location: Location? = null,
    @Json(name = "current") val currentWeather: CurrentWeather? = null,
    @Json(name = "forecast") val forecast: Forecast? = null
)

data class Forecast(@Json(name = "forecastday") val forecastDays: List<ForecastDay>? = null)