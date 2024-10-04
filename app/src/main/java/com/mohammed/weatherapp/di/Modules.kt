package com.mohammed.weatherapp.di

import com.mohammed.weatherapp.BuildConfig
import com.mohammed.weatherapp.data.client.RetrofitClient
import com.mohammed.weatherapp.data.api.WeatherApi
import com.mohammed.weatherapp.data.datasource.local.LocalDataSource
import com.mohammed.weatherapp.data.datasource.local.LocalDataSourceImpl
import com.mohammed.weatherapp.data.datasource.remote.RemoteDataSource
import com.mohammed.weatherapp.data.datasource.remote.RemoteDataSourceImpl
import com.mohammed.weatherapp.repository.WeatherRepository
import com.mohammed.weatherapp.repository.WeatherRepositoryImpl
import com.mohammed.weatherapp.view.ActivityViewModel
import com.mohammed.weatherapp.view.WeatherViewModel
import com.squareup.moshi.Moshi
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val appModule = module {
    single {
        RetrofitClient(BuildConfig.BASE_URL)
    }

    single {
        val client: RetrofitClient = get()
        client.buildMoshiInstance()
    }

    single {
        val client: RetrofitClient = get()
        val moshi: Moshi = get()
        client.buildRetrofitInstance(moshi)
    }

    single<RemoteDataSource> {
        val retrofit = get<Retrofit>()
        RemoteDataSourceImpl(weatherApi = retrofit.create(WeatherApi::class.java))
    }

    single<WeatherRepository> {
        WeatherRepositoryImpl(remoteDataSource = get(), localDataSource = get())
    }

    viewModel {
        ActivityViewModel(repository = get())
    }

    viewModel {
        WeatherViewModel(repository = get())
    }

    single<LocalDataSource> {
        LocalDataSourceImpl(moshi = get(), context = androidContext())
    }

}