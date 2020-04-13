package com.apps.myweatherapp.location.presenter

import com.apps.myweatherapp.location.model.WeatherModel


interface WeatherPresenterContract {
    fun fetchWeather(lat: Double?, lon: Double?)
}

interface WeatherViewContract {
    fun displayWeatherResults(
        searchResults: WeatherModel
    )

    fun displayError()

    fun displayError(s: String?)
}