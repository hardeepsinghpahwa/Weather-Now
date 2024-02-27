package com.example.weatherbook.ui.home

import com.example.weatherbook.io.remote.ApiService
import com.example.weatherbook.model.WeatherData
import retrofit2.Response
import javax.inject.Inject

class HomeRepository @Inject constructor(private val apiService: ApiService) {


    suspend fun getCurrentWeather(latitude: String, longitude: String): Response<WeatherData> {
        return apiService.getCurrentWeather("https://api.open-meteo.com/v1/forecast?latitude=30.437960&longitude=77.619392&current=temperature_2m,apparent_temperature,relative_humidity_2m,is_day,precipitation,rain,showers,snowfall,weather_code,surface_pressure,wind_speed_10m&daily=temperature_2m_min,weather_code,temperature_2m_max&hourly=temperature_2m,relative_humidity_2m,weather_code")
    }
}