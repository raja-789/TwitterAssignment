package com.twitter.challenge.api

import com.twitter.challenge.model.Weather
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("current.json")
    fun getWeatherData(): Call<Weather>

    @GET("future_{day}.json")
    fun getFutureDayWeatherData(@Path("day") day : String): Call<Weather>
}