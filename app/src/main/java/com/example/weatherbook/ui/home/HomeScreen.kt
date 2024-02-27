package com.example.weatherbook.ui.home

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.weatherbook.BR
import com.example.weatherbook.R
import com.example.weatherbook.databinding.ActivityHomeScreenBinding
import com.example.weatherbook.ui.home.adapters.DailyAdapter
import com.example.weatherbook.ui.home.adapters.HourlyAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class HomeScreen : AppCompatActivity() {

    private lateinit var binding: ActivityHomeScreenBinding

    @Inject
    lateinit var viewModelFactory: HomeViewModelFactory

    private val viewModel: HomeViewModel by viewModels { viewModelFactory }

    @Inject
    lateinit var hourlyAdapter: HourlyAdapter

    @Inject
    lateinit var dailyAdapter: DailyAdapter

    @Inject
    lateinit var repository: HomeRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)


        initDataBinding()

        viewModel.getCurrentWeather()

        binding.hourlyData.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.hourlyData.adapter = hourlyAdapter

        binding.dailyData.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.dailyData.adapter = dailyAdapter

        viewModel.hourlyCodes.observe(this) {
            hourlyAdapter.setData(viewModel.hourlyTemps.value!!, it, viewModel.hourlyTimes.value!!)
        }

        viewModel.codeText.observe(this) {
            setDetails(it, binding.tempName, binding.lottieAnimationView)
        }

        viewModel.dailyCodes.observe(this) {
            dailyAdapter.setData(
                viewModel.dailyHighTemps.value!!,
                viewModel.dailyLowTemps.value!!,
                it,
                viewModel.dailyTimes.value!!
            )
        }

    }

    private fun initDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_screen)
        binding.setVariable(BR.viewModel, viewModel)
        binding.executePendingBindings()
    }

    private fun setDetails(code: Int, text: TextView, view: LottieAnimationView) {

        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        Log.d("HOURRRR", hour.toString())
        val isNight = hour < 6 || hour > 18;

        when (code) {

            //clear
            0 -> {
                text.text = "Clear"
                if (isNight) {
                    view.setAnimation(R.raw.clearnight)
                } else {
                    view.setAnimation(R.raw.sunny)
                }
            }

            //Partly cloudy
            1 -> {
                text.text = "Mainly clear"
                if (isNight) {
                    view.setAnimation(R.raw.cloudynight)
                } else {
                    view.setAnimation(R.raw.partlycloudy)
                }
            }

            2 -> {
                text.text = "Partly Cloudy"
                if (isNight) {
                    view.setAnimation(R.raw.cloudynight)
                } else {
                    view.setAnimation(R.raw.partlycloudy)
                }
            }


            3 -> {
                text.text = "Overcast"
                if (isNight) {
                    view.setAnimation(R.raw.cloudynight)
                } else {
                    view.setAnimation(R.raw.partlycloudy)
                }
            }


            //Fog
            45, 48 -> {
                text.text = "Fog"
                view.setAnimation(R.raw.fog)
            }

            //Drizzle
            51, 56 -> {
                text.text = "Light Drizzle"
                view.setAnimation(R.raw.drizzle)
            }

            53 -> {
                text.text = "Moderate Drizzle"
                view.setAnimation(R.raw.drizzle)
            }


            55, 57 -> {
                text.text = "Dense Drizzle"
                view.setAnimation(R.raw.drizzle)
            }


            //Rain
            61, 66 -> {
                text.text = "Light Rain"
                view.setAnimation(R.raw.rain)
            }

            63 -> {
                text.text = "Moderate Rain"
                view.setAnimation(R.raw.rain)
            }


            65, 67 -> {
                text.text = "Heavy Rain"
                view.setAnimation(R.raw.rain)
            }


            //Snow
            71 -> {
                text.text = "Slight Snowfall"
                view.setAnimation(R.raw.snow)
            }

            73 -> {
                text.text = "Moderate Snowfall"
                view.setAnimation(R.raw.snow)
            }


            75 -> {
                text.text = "Heavy Snowfall"
                view.setAnimation(R.raw.snow)
            }

            77 -> {
                text.text = "Snow grains"
                view.setAnimation(R.raw.snow)
            }

            85, 86 -> {
                text.text = "Snow Showers"
                view.setAnimation(R.raw.snow)
            }

            80, 81, 82 -> {
                text.text = "Rain Showers"
                view.setAnimation(R.raw.rain)
            }

            //Thunderstorm
            95 -> {
                text.text = "ThunderStorm"
                view.setAnimation(R.raw.thunder)
            }

            96, 99 -> {
                text.text = "Hail ThunderStorm"
                view.setAnimation(R.raw.thunder)
            }

            else -> {
                text.text = "Clear"
                view.setAnimation(R.raw.sunny)
            }

        }
    }

}