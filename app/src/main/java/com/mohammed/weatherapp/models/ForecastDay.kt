package com.mohammed.weatherapp.models

import com.squareup.moshi.Json

data class ForecastDay(
    val date: String? = null,
    @Json(name = "date_epoch") val dateTimestamp: Long? = null,
    val day: Day? = null,
    val hour: List<Hour>? = null
)