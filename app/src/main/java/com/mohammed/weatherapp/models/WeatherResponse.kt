package com.mohammed.weatherapp.models

import com.squareup.moshi.Json

data class WeatherResponse(
    val location: Location? = null,
    @Json(name = "current") val currentWeather: CurrentWeather? = null
)