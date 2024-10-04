package com.mohammed.weatherapp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mohammed.weatherapp.R
import com.mohammed.weatherapp.databinding.ItemSearchResultBinding
import com.mohammed.weatherapp.models.WeatherSearchResponse

class SearchResultAdapter(private val onSearchResultClicked: (WeatherSearchResponse) -> Unit) :
    ListAdapter<WeatherSearchResponse, ItemViewHolder>(SearchResultDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_result, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position), onSearchResultClicked)
    }
}

class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = ItemSearchResultBinding.bind(itemView)

    fun bind(item: WeatherSearchResponse, onSearchResultClicked: (WeatherSearchResponse) -> Unit) {
        binding.region.text = item.region
        binding.cityName.text = item.name
        binding.root.setOnClickListener { onSearchResultClicked(item) }
    }
}

private class SearchResultDiff : DiffUtil.ItemCallback<WeatherSearchResponse>() {
    override fun areItemsTheSame(
        oldItem: WeatherSearchResponse,
        newItem: WeatherSearchResponse
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: WeatherSearchResponse,
        newItem: WeatherSearchResponse
    ): Boolean {
        return oldItem.name == newItem.name
    }
}