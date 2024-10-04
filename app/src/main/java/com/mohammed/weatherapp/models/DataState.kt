package com.mohammed.weatherapp.models

sealed class DataState<out T> {
    data class Success<T>(val data: T, val isRemoteData: Boolean) : DataState<T>()
    data class Error<T>(val message: String, val exception: Exception? = null, val isRemoteData: Boolean) : DataState<T>()
}