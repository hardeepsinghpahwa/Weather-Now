package com.example.weatherbook.ui.home

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class HomeViewModel(private val repository: HomeRepository) : ViewModel() {

    val loader = ObservableField(false)
    val retry = ObservableField(false)

    val hourlyTemps = MutableLiveData<ArrayList<Double>>()
    val dailyLowTemps = MutableLiveData<ArrayList<Double>>()
    val dailyHighTemps = MutableLiveData<ArrayList<Double>>()
    val hourlyCodes = MutableLiveData<ArrayList<Int>>()
    val dailyCodes = MutableLiveData<ArrayList<Int>>()
    val hourlyTimes = MutableLiveData<ArrayList<String>>()
    val dailyTimes = MutableLiveData<ArrayList<String>>()

    val updatedTime = ObservableField("")
    val placeName = ObservableField("")
    val temp = ObservableField("")
    val codeText = MutableLiveData<Int>()
    val wind = ObservableField("")
    val humidity = ObservableField("")
    val feelsLike = ObservableField("")

    //var hourlyData

    fun getCurrentWeather(lat: String, lon: String) {
        loader.set(true)
        retry.set(false)


        viewModelScope.launch(Dispatchers.IO) {

            try {
                val response = repository.getCurrentWeather(lat, lon)

                if (response.code() == 200) {

                    val data = response.body()

                    if (data != null) {
                        temp.set(
                            data.current.temperatureM.roundToInt()
                                .toString() + data.currentUnits.temperatureM
                        )
                        wind.set(data.current.windSpeedM.toString() + data.currentUnits.windSpeedM)
                        humidity.set(data.current.relativeHumidityM.toString() + data.currentUnits.relativeHumidityM)
                        feelsLike.set(
                            data.current.apparentTemperature.roundToInt()
                                .toString() + data.currentUnits.temperatureM
                        )

                        hourlyTimes.postValue(data.hourly.time!!)
                        hourlyTemps.postValue(data.hourly.temperature2M!!)
                        hourlyCodes.postValue(data.hourly.weatherCode!!)

                        dailyTimes.postValue(data.daily.time!!)
                        dailyHighTemps.postValue(data.daily.temperatureMMax!!)
                        dailyLowTemps.postValue(data.daily.temperatureMMin!!)
                        dailyCodes.postValue(data.daily.weatherCode!!)

                        codeText.postValue(data.current.weatherCode)

                        //loader.set(false)
                    }

                } else {
                    loader.set(false)
                    retry.set(true)
                }

            } catch (_: Exception) {
                retry.set(true)
                loader.set(false)
            }
        }
    }
}