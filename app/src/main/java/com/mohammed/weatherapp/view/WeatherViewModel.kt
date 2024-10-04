package com.mohammed.weatherapp.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohammed.weatherapp.models.DataState
import com.mohammed.weatherapp.models.ForecastResponse
import com.mohammed.weatherapp.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel(){

    val weatherSate = MutableSharedFlow<DataState<ForecastResponse>>(replay = 1)

    fun getWeather(location: String, isCurrentLocation: Boolean = false) {
        viewModelScope.launch {
            val response = repository.getForecast(location, isCurrentLocation)
            weatherSate.emit(response)
        }
    }

    fun getQueryState() = repository.getQueryState()



}