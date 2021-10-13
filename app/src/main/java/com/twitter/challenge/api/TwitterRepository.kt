package com.twitter.challenge.api

import com.twitter.challenge.model.Weather
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TwitterRepository {

    // GET repo list
    fun getWeatherData(onResult: (isSuccess: Boolean, response: Weather?) -> Unit) {
        ApiClient.instance.getWeatherData().enqueue(object : Callback<Weather> {
            override fun onResponse(call: Call<Weather>?, response: Response<Weather>?) {
                if (response != null && response.isSuccessful)
                    onResult(true, response.body()!!)
                else
                    onResult(false, null)
            }
            override fun onFailure(call: Call<Weather>?, t: Throwable?) {
                onResult(false, null)
            }

        })
    }

    fun getFutureDayWeatherData(day : String, onResult: (isSuccess: Boolean, response: Weather?) -> Unit) {
        ApiClient.instance.getFutureDayWeatherData(day).enqueue(object : Callback<Weather> {
            override fun onResponse(call: Call<Weather>?, response: Response<Weather>?) {
                if (response != null && response.isSuccessful)
                    onResult(true, response.body()!!)
                else
                    onResult(false, null)
            }
            override fun onFailure(call: Call<Weather>?, t: Throwable?) {
                onResult(false, null)
            }

        })
    }

    companion object {
        private var INSTANCE: TwitterRepository? = null
        fun getInstance() = INSTANCE
                ?: TwitterRepository().also {
                    INSTANCE = it
                }
    }
}