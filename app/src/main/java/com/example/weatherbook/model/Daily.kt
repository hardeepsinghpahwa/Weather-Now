package com.example.weatherbook.model

import com.google.gson.annotations.SerializedName

data class Daily(    @SerializedName("temperature_2m_max")
                     val temperatureMMax: ArrayList<Double>?,
                     @SerializedName("temperature_2m_min")
                     val temperatureMMin: ArrayList<Double>?,
                 val time: ArrayList<String>?,
                     @SerializedName("weather_code")
                     val weatherCode: ArrayList<Int>?)