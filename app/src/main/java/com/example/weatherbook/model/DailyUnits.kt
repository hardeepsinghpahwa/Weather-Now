package com.example.weatherbook.model

import com.google.gson.annotations.SerializedName

data class DailyUnits(
    @SerializedName("temperature_2m_max")
    val temperatureMMax: String = "",
    @SerializedName("temperature_2m_min")
    val temperatureMMin: String = "",
    val time: String = "",
    @SerializedName("weather_code")
    val weatherCode: String = ""
)