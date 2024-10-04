package com.mohammed.weatherapp.models

import com.squareup.moshi.Json

data class CurrentWeather (
    @Json(name="temp_c") val tempC: Double? = null,
    @Json(name="temp_f") val tempF: Double? = null,
    val condition: WeatherCondition? = null,
    @Json(name="wind_mph") val windMph: Double? = null,
    @Json(name="wind_kph") val windKph: Double? = null,
    @Json(name="wind_degree") val windDegree: Double? = null,
    @Json(name="wind_dir") val windDir: String? = null,
    val humidity: Double? = null,
)