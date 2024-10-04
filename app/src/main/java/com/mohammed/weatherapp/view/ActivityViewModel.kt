package com.mohammed.weatherapp.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohammed.weatherapp.models.DataState
import com.mohammed.weatherapp.models.WeatherSearchResponse
import com.mohammed.weatherapp.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class ActivityViewModel(private val repository: WeatherRepository) : ViewModel() {

    val weatherSearchState: MutableSharedFlow<DataState<List<WeatherSearchResponse>>> = MutableSharedFlow(replay = 1)

    fun posWeatherQueryState(query: String) {
        viewModelScope.launch {
            repository.postQueryState(query)
        }
    }

    private var searchJob: Job? = null

    fun searchWeather(searchFlow: Flow<String>) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            searchFlow.collect { searchQuery ->
                // Use the search query to get the weather
                val weather = repository.search(searchQuery)
                weatherSearchState.emit(weather)
            }
        }
    }


}