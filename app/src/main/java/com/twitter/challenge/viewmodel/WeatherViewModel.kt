package com.twitter.challenge.viewmodel

import androidx.lifecycle.MutableLiveData
import com.twitter.challenge.api.BaseViewModel
import com.twitter.challenge.api.TwitterRepository
import com.twitter.challenge.model.Weather

class WeatherViewModel : BaseViewModel() {

    val weatherLiveData = MutableLiveData<Weather>()

    val futureDayWeatherLiveData = MutableLiveData<Weather>()

    fun fetchWeatherData() {
        dataLoading.value = true
        TwitterRepository.getInstance().getWeatherData { isSuccess, response ->
            dataLoading.value = false
            if (isSuccess) {
                weatherLiveData.value = response?.let { it }
                empty.value = weatherLiveData.value == null
            } else {
                empty.value = true
            }
        }
    }

    fun fetchFutureDayWeatherData(day: String) {
        TwitterRepository.getInstance().getFutureDayWeatherData(day) { isSuccess, response ->
            if (isSuccess) {
                futureDayWeatherLiveData.value = response?.let { it }
                empty.value = futureDayWeatherLiveData.value == null
            } else {
                empty.value = true
            }
        }
    }
}