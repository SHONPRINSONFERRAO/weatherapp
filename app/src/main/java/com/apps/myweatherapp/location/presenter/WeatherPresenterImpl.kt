package com.apps.myweatherapp.location.presenter

import com.apps.myweatherapp.location.model.WeatherModel
import com.apps.myweatherapp.network.NetworkRepository
import com.apps.myweatherapp.search.model.CityDataModel
import retrofit2.Response

class WeatherPresenterImpl(
    val view: WeatherViewContract,
    private val repository: NetworkRepository
) :WeatherPresenterContract, NetworkRepository.RepositoryCallback {




    override fun fetchWeather(lat: Double?, lon: Double?) {
        if(lat == null || lon == null)
        {
            this.handleError("Error loading data...")
        } else {
            repository.loadWeatherData(lat,lon,this)
        }

    }

    override fun handleWeatherResponse(response: Response<WeatherModel>) {

        if (response.isSuccessful) {
            val dataResponse = response?.body()
            if (dataResponse != null) {
                view.displayWeatherResults(dataResponse)
            } else {
                view.displayError("Invalid Response")
            }
        } else {
            view.displayError("Failed to fetch error")
        }
    }

    override fun handleSearchWeatherResponse(response: Response<CityDataModel>) {
    }

    override fun handleError() {
        view.displayError()
    }

    override fun handleError(reason: String) {
        view.displayError(reason)
    }

}
