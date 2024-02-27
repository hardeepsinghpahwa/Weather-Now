package com.example.weatherbook.ui.home.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.weatherbook.R
import com.example.weatherbook.databinding.ItemDailyBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject
import kotlin.math.roundToInt

class DailyAdapter @Inject constructor() : RecyclerView.Adapter<DailyAdapter.ListViewHolder>() {

    private var dailyHighTemps = ArrayList<Double>()
    private var dailyLowTemps = ArrayList<Double>()
    private var dailyCodes = ArrayList<Int>()
    private var dailyTimes = ArrayList<String>()

    inner class ListViewHolder(val binding: ItemDailyBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layout = ItemDailyBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ListViewHolder(layout)
    }

    fun setData(
        dailyHighTemps: ArrayList<Double>,
        dailyLowTemps: ArrayList<Double>,
        dailyCodes: ArrayList<Int>,
        dailyTimes: ArrayList<String>
    ) {
        this.dailyHighTemps = dailyHighTemps
        this.dailyLowTemps = dailyLowTemps
        this.dailyCodes = dailyCodes
        this.dailyTimes = dailyTimes

        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dailyTimes.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        with(holder) {
            with(dailyHighTemps[position]) {
                binding.highTemp.text = "${this.roundToInt()}°C"
            }

            with(dailyCodes[position]) {
                binding.icon.setImageDrawable(getIcon(this, binding.icon.context))
            }

            with(dailyLowTemps[position]) {
                binding.lowTemp.text = "${this.roundToInt()}°C"
            }

            with(dailyTimes[position]) {

                val dateFormat = SimpleDateFormat("yyyy-MM-dd")

                val dateTime = SimpleDateFormat("yyyy-MM-dd").parse(this)

                val dayFormat = SimpleDateFormat("EEE")

                var day = ""

                if (this == dateFormat.format(Calendar.getInstance().time)) {
                    day = "Today"
                } else {
                    day = dayFormat.format(dateTime)
                }

                binding.time.text = day
            }

        }
    }

    private fun getIcon(code: Int, context: Context): Drawable? {

        return when (code) {

            //clear
            0 -> {
                ContextCompat.getDrawable(context, R.drawable.sun)
            }

            //Partly cloudy
            1, 2, 3 -> {
                ContextCompat.getDrawable(context, R.drawable.partlycloudy)
            }

            //Fog
            45, 48 -> {
                ContextCompat.getDrawable(context, R.drawable.cloudyicon)
            }

            //Drizzle
            51, 53, 55, 56, 57 -> {
                ContextCompat.getDrawable(context, R.drawable.drizzle)
            }

            //Rain
            61, 63, 65, 66, 67 -> {
                ContextCompat.getDrawable(context, R.drawable.rainicon)
            }

            //Snow
            71, 73, 75, 77, 85, 86 -> {
                ContextCompat.getDrawable(context, R.drawable.snowicon)
            }

            //Thunderstorm
            95, 96, 99 -> {
                ContextCompat.getDrawable(context, R.drawable.thunderstormicon)
            }

            else -> {
                ContextCompat.getDrawable(context, R.drawable.cloudyicon)
            }

        }
    }

}