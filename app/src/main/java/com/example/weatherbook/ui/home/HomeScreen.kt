package com.example.weatherbook.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.weatherbook.BR
import com.example.weatherbook.R
import com.example.weatherbook.databinding.ActivityHomeScreenBinding
import com.example.weatherbook.ui.home.adapters.DailyAdapter
import com.example.weatherbook.ui.home.adapters.HourlyAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer


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

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    var latitudeSet = ""
    var longitudeSet = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        initDataBinding()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        //viewModel.getCurrentWeather()
        getUpdatedTime()
        checkLocationPermission()

        binding.hourlyData.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.hourlyData.adapter = hourlyAdapter

        binding.dailyData.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.dailyData.adapter = dailyAdapter

        viewModel.hourlyCodes.observe(this) {
            getUpdatedTime()
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
            if (binding.swiperefresh.isRefreshing) {
                binding.swiperefresh.isRefreshing = false
            }
            viewModel.loader.set(false)
        }

        binding.swiperefresh.setOnRefreshListener {
            setUpdatedTime()
            if(latitudeSet.isNotEmpty()) {
                viewModel.getCurrentWeather(latitudeSet,longitudeSet)
            }
        }


        if (isNight()) {
            binding.background.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.night
                )
            )
        }

        fixedRateTimer("timer", false, 0, 60000) {
            this@HomeScreen.runOnUiThread {
                getUpdatedTime()
            }
        }

        binding.location.setOnClickListener {
            checkLocationPermission()
        }
    }

    private fun isNight(): Boolean {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return hour < 6 || hour > 18
    }

    private fun initDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_screen)
        binding.setVariable(BR.viewModel, viewModel)
        binding.executePendingBindings()
    }

    private fun setDetails(code: Int, text: TextView, view: LottieAnimationView) {
        when (code) {

            //clear
            0 -> {
                text.text = "Clear"
                if (isNight()) {
                    view.setAnimation(R.raw.clearnight)
                } else {
                    view.setAnimation(R.raw.sunny)
                }
            }

            //Partly cloudy
            1 -> {
                text.text = "Mainly clear"
                if (isNight()) {
                    view.setAnimation(R.raw.cloudynight)
                } else {
                    view.setAnimation(R.raw.partlycloudy)
                }
            }

            2 -> {
                text.text = "Partly Cloudy"
                if (isNight()) {
                    view.setAnimation(R.raw.cloudynight)
                } else {
                    view.setAnimation(R.raw.partlycloudy)
                }
            }


            3 -> {
                text.text = "Overcast"
                if (isNight()) {
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

    private fun setUpdatedTime() {
        val pref = applicationContext.getSharedPreferences("MyPref", 0) // 0 - for private mode

        pref.edit {
            this.putLong("updated_time", Calendar.getInstance().time.time)
        }
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            askLocationPermission()
        } else {
            fusedLocationClient.lastLocation
                .addOnSuccessListener {
                    viewModel.placeName.set(getLocationName(it!!.latitude, it.longitude))
                }
        }
    }

    private fun askLocationPermission() {
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        Toast.makeText(
                            applicationContext,
                            "Grant location permission",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@registerForActivityResult
                    }
                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { loc: Location? ->
                            viewModel.placeName.set(getLocationName(loc!!.latitude, loc.longitude))
                        }
                }

                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { loc: Location? ->
                            viewModel.placeName.set(getLocationName(loc!!.latitude, loc.longitude))
                        }
                }

                else -> {
                    Toast.makeText(
                        applicationContext,
                        "Grant location permission",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun getUpdatedTime() {
        val pref = applicationContext.getSharedPreferences("MyPref", 0) // 0 - for private mode

        val time = pref.getLong("updated_time", 0)

        if (time > 0) {
            Log.d("TIME___", "$time")
            Log.d("TIME___CURRENT", "${Calendar.getInstance().time.time}")
            var different = Calendar.getInstance().time.time - time

            val secondsInMilli = 1000
            val minutesInMilli = secondsInMilli * 60
            val hoursInMilli = minutesInMilli * 60
            val daysInMilli = hoursInMilli * 24

            val elapsedDays = different / daysInMilli
            different %= daysInMilli;

            val elapsedHours = different / hoursInMilli
            different %= hoursInMilli;

            val elapsedMinutes = different / minutesInMilli
            different %= minutesInMilli;

            val elapsedSeconds = different / secondsInMilli

            if (elapsedDays > 0) {
                viewModel.updatedTime.set("Updated $elapsedDays days ago")
            } else if (elapsedHours > 0) {
                viewModel.updatedTime.set("Updated $elapsedHours hours ago")
            } else if (elapsedMinutes > 0) {
                viewModel.updatedTime.set("Updated $elapsedMinutes minutes ago")
            } else if (elapsedSeconds > 0) {
                viewModel.updatedTime.set("Updated $elapsedSeconds seconds ago")
            }
        }
    }

    private fun getLocationName(latitude: Double, longitude: Double): String {
        latitudeSet = latitude.toString()
        longitudeSet = longitude.toString()
        setUpdatedTime()
        viewModel.getCurrentWeather(latitude.toString(), longitude.toString())
        val geocoder = Geocoder(this, Locale.getDefault());
        var locationName = ""

        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size > 0) {
                val address = addresses[0];
                locationName = address.locality
            }
        } catch (e: IOException) {
            e.printStackTrace();
        }

        return locationName;
    }


}