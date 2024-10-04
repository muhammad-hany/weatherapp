package com.mohammed.weatherapp.data.datasource.local

import android.content.Context
import com.mohammed.weatherapp.models.DataState
import com.mohammed.weatherapp.models.ForecastResponse
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class LocalDataSourceImpl(private val moshi: Moshi, private val context: Context) :
    LocalDataSource {

    override suspend fun saveWeatherForecast(weatherForecast: ForecastResponse) =
        withContext(Dispatchers.IO) {
            val jsonAdapter = moshi.adapter(ForecastResponse::class.java)
            val objectString = jsonAdapter.toJson(weatherForecast)
            try {
                val file = File(context.cacheDir, WEATHER_FORECAST_KEY)
                val outputStreamWriter = OutputStreamWriter(FileOutputStream(file))
                val bufferedWriter = BufferedWriter(outputStreamWriter)
                bufferedWriter.use { it.write(objectString) }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

    override suspend fun checkIfWeatherForecastExists(): Boolean {
        return File(context.cacheDir, WEATHER_FORECAST_KEY).exists()
    }

    override suspend fun getWeatherForecast(): DataState<ForecastResponse> = withContext(Dispatchers.IO) {
        if (!checkIfWeatherForecastExists()) return@withContext DataState.Error("File does not exist", null, false)
        val stringBuilder = StringBuilder()
        val jsonAdapter = moshi.adapter(ForecastResponse::class.java)
        try {
            val file = File(context.cacheDir, WEATHER_FORECAST_KEY)
            val inputStreamReader = InputStreamReader(FileInputStream(file))
            val bufferReader = BufferedReader(inputStreamReader)
            bufferReader.useLines { lines ->
                lines.forEach { stringBuilder.append(it) }
            }
            val cachedForecast = jsonAdapter.fromJson(stringBuilder.toString())
            if (cachedForecast != null) {
                return@withContext DataState.Success(cachedForecast, true)
            } else {
                return@withContext DataState.Error("Error while reading file", null, false)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext DataState.Error("Error while reading file", e, false)
        }
    }


    companion object {
        const val WEATHER_FORECAST_KEY = "weather_forecast.txt"
    }
}