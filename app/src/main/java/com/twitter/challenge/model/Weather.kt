package com.twitter.challenge.model

data class Weather(val coord : Coord, val weather : WeatherData, val wind : Wind, val clouds : Clouds, val name: String)


data class Coord (val lon: Double, val lat: Double)

data class WeatherData(val temp: Double, val pressure: Long, val humidity : Long)

data class Wind (val speed: Double, val deg: Double)

data class Clouds (val cloudiness: Double)

