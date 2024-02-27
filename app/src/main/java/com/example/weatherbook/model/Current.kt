package com.example.weatherbook.model

import com.google.gson.annotations.SerializedName

data class Current(
    @SerializedName("wind_speed_10m")
    val windSpeedM: Double = 0.0,
    @SerializedName("temperature_2m")
    val temperatureM: Double = 0.0,
    val precipitation: Double = 0.0,
    @SerializedName("apparent_temperature")
    val apparentTemperature: Double = 0.0,
    val rain: Double = 0.0,
    val showers: Double = 0.0,
    @SerializedName("surface_pressure")
    val surfacePressure: Double = 0.0,
    @SerializedName("relative_humidity_2m")
    val relativeHumidityM: Double = 0.0,
    val snowfall: Double = 0.0,
    @SerializedName("is_day")
    val isDay: Int = 0,
    val interval: Int = 0,
    val time: String = "",
    @SerializedName("weather_code")
    val weatherCode: Int = 0
)