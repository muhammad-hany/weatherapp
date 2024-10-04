package com.mohammed.weatherapp.view

import android.icu.text.SimpleDateFormat
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mohammed.weatherapp.R
import com.mohammed.weatherapp.databinding.ForecastListItemBinding
import com.mohammed.weatherapp.models.ForecastDay
import java.util.Date
import java.util.Locale

class ForecastAdapter : ListAdapter<ForecastDay, ForecastViewHolder>(ForecastDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.forecast_list_item, parent, false)
        return ForecastViewHolder(view)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}

class ForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = ForecastListItemBinding.bind(itemView)

    fun onBind(item: ForecastDay) {
        binding.maxTemp.text =
            itemView.context.getString(R.string.temp, item.day?.maxTempC.toString())
        binding.minTemp.text =
            itemView.context.getString(R.string.temp, item.day?.minTempC.toString())
        binding.condition.text = item.day?.condition?.text
        item.dateTimestamp?.let { timestamp ->
            val isToday = DateUtils.isToday(timestamp * 1000L)
            if (isToday) {
                binding.date.text = itemView.context.getString(R.string.today)
                return
            }
            val isTomorrow = DateUtils.isToday(timestamp * 1000L - DateUtils.DAY_IN_MILLIS)
            if (isTomorrow) {
                binding.date.text = itemView.context.getString(R.string.tomorrow)
                return
            }
            val isYesterday = DateUtils.isToday(timestamp * 1000L + DateUtils.DAY_IN_MILLIS)
            if (isYesterday) {
                binding.date.text = itemView.context.getString(R.string.yesterday)
                return
            }
            val date = Date(timestamp * 1000L)
            val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
            val day = dayFormat.format(date)
            binding.date.text = day
        }
    }
}

private class ForecastDiff : DiffUtil.ItemCallback<ForecastDay>() {
    override fun areItemsTheSame(oldItem: ForecastDay, newItem: ForecastDay): Boolean {
        return oldItem.date == newItem.date
    }

    override fun areContentsTheSame(oldItem: ForecastDay, newItem: ForecastDay): Boolean {
        return oldItem.day == newItem.day
    }
}