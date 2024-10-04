package com.mohammed.weatherapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationServices
import com.mohammed.weatherapp.databinding.FragmentCurrentWeatherBinding
import com.mohammed.weatherapp.models.CurrentWeather
import com.mohammed.weatherapp.models.Location
import com.mohammed.weatherapp.view.ForecastAdapter
import com.mohammed.weatherapp.view.WeatherViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class WeatherFragment : Fragment() {

    private var _binding: FragmentCurrentWeatherBinding? = null
    private val viewModel: WeatherViewModel by viewModel()
    private val binding get() = _binding!!

    private val locationPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            when {
                permissions.getOrDefault(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    false
                ) || permissions.getOrDefault(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    false
                ) -> {
                    getCurrentLocationWeather()
                }

                else -> {
                    // No location access granted.
                    Toast.makeText(
                        requireContext(),
                        "Location permission is required to get the weather",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val forecastAdapter = ForecastAdapter()
        binding.forecastList.adapter = forecastAdapter
        binding.forecastList.layoutManager = LinearLayoutManager(requireContext())
        getCurrentLocationWeather()
        setupCollectors(forecastAdapter)
    }

    private fun setupCollectors(forecastAdapter: ForecastAdapter) {
        lifecycleScope.launch {
            viewModel.weatherSate.collectLatest { forecastResponse ->
                if (forecastResponse?.currentWeather != null && forecastResponse.location != null) {
                    updateCurrentWeather(forecastResponse.currentWeather, forecastResponse.location)
                    forecastResponse.forecast?.forecastDays?.let {
                        forecastAdapter.submitList(it)
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.getQueryState().collectLatest { query ->
                query?.let {
                    binding.progressBar.visibility = View.VISIBLE
                    if (it.isNotEmpty()) {
                        viewModel.getWeather(it)
                    } else {
                        getCurrentLocationWeather()
                    }
                }
            }
        }
    }

    private fun updateCurrentWeather(currentWeather: CurrentWeather, location: Location) {
        binding.progressBar.visibility = View.GONE
        binding.wind.text = "wind speed ${currentWeather.windKph} km/h"
        binding.region.text = location.region
        binding.temp.text = "${currentWeather.tempC.toString()}°C"
        binding.humidty.text = "Humidity: ${currentWeather.humidity}%"
        binding.weatherCondition.text = currentWeather.condition?.text
        binding.country.text = location.country
    }

    private fun getCurrentLocationWeather() {
        if (!locationPermissionGranted()) return
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                viewModel.getWeather("$latitude,$longitude", true)
            }
        }
    }

    private fun locationPermissionGranted() : Boolean{
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            return false
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}