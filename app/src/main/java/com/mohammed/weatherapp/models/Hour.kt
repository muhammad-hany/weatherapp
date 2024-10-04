package com.mohammed.weatherapp.models

import com.squareup.moshi.Json

data class Hour(
    @Json(name = "time_epoch") val timeEpoch: Long? = null,
    @Json(name = "time") val time: String? = null,
    @Json(name = "temp_c") val tempC: Double? = null,
    @Json(name = "temp_f") val tempF: Double? = null,
    val condition: WeatherCondition? = null,
    @Json(name = "wind_mph") val windMph: Double? = null,
    @Json(name = "wind_kph") val windKph: Double? = null,
    @Json(name = "wind_degree") val windDegree: Double? = null,
    @Json(name = "wind_dir") val windDir: String? = null,
    val humidity: Double? = null,
    @Json(name = "will_it_rain") val willItRain: Int? = null,
    @Json(name = "chance_of_rain") val chanceOfRain: Double? = null,
)