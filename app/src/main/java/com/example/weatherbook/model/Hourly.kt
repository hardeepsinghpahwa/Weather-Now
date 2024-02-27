package com.example.weatherbook.model

import com.google.gson.annotations.SerializedName

data class Hourly(
    @SerializedName("temperature_2m")
    val temperature2M: ArrayList<Double>?,
    val time: ArrayList<String>?,
    @SerializedName("weather_code")
    val weatherCode: ArrayList<Int>?
)