package com.mohammed.weatherapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mohammed.weatherapp.databinding.ActivityMainBinding
import com.mohammed.weatherapp.models.DataState
import com.mohammed.weatherapp.models.WeatherSearchResponse
import com.mohammed.weatherapp.view.ActivityViewModel
import com.mohammed.weatherapp.view.SearchResultAdapter
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

@OptIn(FlowPreview::class)
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: ActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.searchBar)
        val searchResultAdapter = SearchResultAdapter(::onLocationSelected)
        setSearchView(searchResultAdapter)
        binding.searchResults.adapter = searchResultAdapter
        binding.searchResults.layoutManager = LinearLayoutManager(this)
        setupCollectors(searchResultAdapter)
        handleBackAction()
    }

    private fun handleBackAction() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.searchView.isShowing) {
                    binding.searchView.hide()
                    binding.searchProgressbar.visibility = View.INVISIBLE
                } else {
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    private fun setupCollectors(searchResultAdapter: SearchResultAdapter) {
        lifecycleScope.launch {
            viewModel.weatherSearchState.collectLatest { weatherResponse ->
                when (weatherResponse) {
                    is DataState.Success -> {
                        binding.searchProgressbar.visibility = View.INVISIBLE
                        if (binding.searchView.editText.text.isNullOrEmpty() || binding.searchView.editText.text.length < 3) {
                            searchResultAdapter.submitList(null)
                        } else {
                            searchResultAdapter.submitList(weatherResponse.data)
                        }
                    }
                    is DataState.Error -> {
                        binding.searchProgressbar.visibility = View.INVISIBLE
                        Toast.makeText(this@MainActivity, "An error occurred", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

            }
        }
    }

    private fun onLocationSelected(weatherSearch: WeatherSearchResponse) {
        binding.searchView.hide()
        binding.searchBar.setText(weatherSearch.region)
        if (!weatherSearch.region.isNullOrEmpty()) viewModel.posWeatherQueryState(weatherSearch.region)
        binding.searchBar.navigationIcon =
            AppCompatResources.getDrawable(this, R.drawable.ic_close)
    }

    private fun setSearchView(searchResultAdapter: SearchResultAdapter) {
        // converting user input to a flow to easily process the search query
        val searchFlow = callbackFlow {
            binding.searchView.editText.doAfterTextChanged { searchQuery ->
                if (searchQuery.isNullOrEmpty() || searchQuery.length < 3) {
                    searchResultAdapter.submitList(emptyList())
                    return@doAfterTextChanged
                }
                binding.searchProgressbar.visibility = View.VISIBLE
                trySend(searchQuery.toString())
            }
            awaitClose()
        }.debounce(700) // debounce to wait for the user to finish typing and not making unnecessary api calls
        viewModel.searchWeather(searchFlow)

        //resetting search query to current location weather
        binding.searchBar.setNavigationOnClickListener {
            // sending empty string to display current location weather
            viewModel.posWeatherQueryState("")
            //reset the search bar icon
            binding.searchBar.navigationIcon =
                AppCompatResources.getDrawable(this, R.drawable.ic_search)
            //reset search hint
            binding.searchBar.setText(null)
        }
    }

}