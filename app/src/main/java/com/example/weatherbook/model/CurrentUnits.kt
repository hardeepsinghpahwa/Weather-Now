package com.example.weatherbook.model

import com.google.gson.annotations.SerializedName

data class CurrentUnits(
    @SerializedName("wind_speed_10m") val windSpeedM: String = "",
    @SerializedName("temperature_2m") val temperatureM: String = "",
    val precipitation: String = "",
    val rain: String = "",
    val showers: String = "",
    @SerializedName("surface_pressure") val surfacePressure: String = "",
    @SerializedName("relative_humidity_2m") val relativeHumidityM: String = "",
    val snowfall: String = "",
    @SerializedName("is_day") val isDay: String = "",
    val interval: String = "",
    val time: String = "",
    @SerializedName("weather_code") val weatherCode: String = ""
)