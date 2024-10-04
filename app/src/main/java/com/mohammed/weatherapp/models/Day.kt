package com.mohammed.weatherapp.models

import com.squareup.moshi.Json

data class Day(
    @Json(name = "maxtemp_c") val maxTempC: Double? = null,
    @Json(name = "maxtemp_f") val maxTempF: Double? = null,
    @Json(name = "mintemp_c") val minTempC: Double? = null,
    @Json(name = "mintemp_f") val minTempF: Double? = null,
    @Json(name = "avgtemp_c") val avgTampC: Double? = null,
    @Json(name = "avgtemp_f") val avgTempF: Double? = null,
    @Json(name = "maxwind_mph") val maxWindMph: Double? = null,
    @Json(name = "maxwind_kph") val maxWindKph: Double? = null,
    @Json(name = "avghumidity") val averageHumidity: Double? = null,
    @Json(name = "daily_will_it_rain") val dailyWillItRain: Int? = null,
    @Json(name = "daily_chance_of_rain") val dailyChanceOfRain: String? = null,
    @Json(name = "daily_will_it_snow") val dailyWillItSnow: Int? = null,
    @Json(name = "daily_chance_of_snow") val dailyChanceOfSnow: String? = null,
    @Json(name = "condition") val condition: WeatherCondition? = null
)