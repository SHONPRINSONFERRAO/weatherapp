package com.apps.myweatherapp.search.presenter

import com.apps.myweatherapp.location.model.WeatherModel
import com.apps.myweatherapp.search.model.CityDataModel

interface SearchPresenterContract {
    fun fetchSearchResults(city: String?)
}

interface SearchViewContract {
    fun displaySearchResults(
        searchResults: CityDataModel
    )

    fun displayError()

    fun displayError(s: String?)
}