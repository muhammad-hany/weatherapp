package com.mohammed.weatherapp

import android.app.Application
import com.mohammed.weatherapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin

class WeatherApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@WeatherApp)
            loadKoinModules(appModule)
        }
    }
}