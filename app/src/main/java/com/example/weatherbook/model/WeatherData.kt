package com.example.weatherbook.model

import com.google.gson.annotations.SerializedName

data class WeatherData(
    val elevation: Int = 0,
    @SerializedName("generationtime_ms") val generationtimeMs: Double = 0.0,
    val current: Current,
    @SerializedName("timezone_abbreviation") val timezoneAbbreviation: String = "",
    @SerializedName("current_units")
    val currentUnits: CurrentUnits,
    @SerializedName("daily_units") val dailyUnits: DailyUnits,
    @SerializedName("hourly") val hourly: Hourly,
    val timezone: String = "",
    val latitude: Double = 0.0,
    val daily: Daily,
    @SerializedName("utc_offset_seconds") val utcOffsetSeconds: Int = 0,
    val longitude: Double = 0.0
)