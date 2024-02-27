package com.example.weatherbook.ui.home.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.weatherbook.R
import com.example.weatherbook.databinding.ItemHourlyBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject
import kotlin.math.roundToInt

class HourlyAdapter @Inject constructor() : RecyclerView.Adapter<HourlyAdapter.ListViewHolder>() {

    private var hourlyTemps = ArrayList<Double>()
    private var hourlyCodes = ArrayList<Int>()
    private var hourlyTimes = ArrayList<String>()

    inner class ListViewHolder(val binding: ItemHourlyBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layout = ItemHourlyBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ListViewHolder(layout)
    }

    fun setData(
        hourlyTemps: ArrayList<Double>,
        hourlyCodes: ArrayList<Int>,
        hourlyTimes: ArrayList<String>
    ) {
        this.hourlyTemps = hourlyTemps
        this.hourlyCodes = hourlyCodes
        this.hourlyTimes = hourlyTimes

        var i = 0
        while (i != hourlyTemps.size) {

            try {
                val dateTime = SimpleDateFormat("yyyy-MM-dd'T'hh:mm").parse(hourlyTimes[i])

                val dateFormat = SimpleDateFormat("yyyy-MM-dd")

                val time = Calendar.getInstance()

                time.time = dateTime!!


                if (dateFormat.format(dateTime)
                        .trim() == dateFormat.format(Calendar.getInstance().time).trim()
                ) {
                    if (!time.time.after(Calendar.getInstance().time)) {
                        hourlyCodes.removeAt(i)
                        hourlyTimes.removeAt(i)
                        hourlyTemps.removeAt(i)
                    } else {
                        i += 1
                    }
                } else {
                    i += 1
                }

            } catch (e: IndexOutOfBoundsException) {
                break
            }
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return hourlyTimes.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        with(holder) {
            with(hourlyTemps[position]) {
                binding.temp.text = "${this.roundToInt()}°C"
            }
            with(hourlyCodes[position]) {
                binding.icon.setImageDrawable(getIcon(this, binding.icon.context))
            }

            with(hourlyTimes[position]) {

                val dateTime = SimpleDateFormat("yyyy-MM-dd'T'hh:mm").parse(this)

                val dateFormat = SimpleDateFormat("yyyy-MM-dd")

                val dayFormat = SimpleDateFormat("EEE")

                val time = Calendar.getInstance()

                time.time = dateTime!!

                var day = ""

                if (dateFormat.format(dateTime) == dateFormat.format(Calendar.getInstance().time)) {
                    if (time.time.after(Calendar.getInstance().time)) {
                        day = "Today"
                    }
                } else {
                    day = dayFormat.format(dateTime)
                }

                binding.time.text = day + "\n" + SimpleDateFormat("hh:mm a").format(dateTime)
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