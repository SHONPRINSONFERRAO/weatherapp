package com.apps.myweatherapp.search.presenter

import com.apps.myweatherapp.location.model.WeatherModel
import com.apps.myweatherapp.network.NetworkRepository
import com.apps.myweatherapp.search.model.CityDataModel
import retrofit2.Response

class SearchPresenterImpl(
    val view: SearchViewContract,
    private val repository: NetworkRepository
) : SearchPresenterContract, NetworkRepository.RepositoryCallback {

    override fun fetchSearchResults(str: String?) {
        if(str == null)
        {
            this.handleError("Error loading data...")
        } else {
            repository.loadCityData(str,this)
        }
    }

    override fun handleWeatherResponse(response: Response<WeatherModel>) {

    }

    override fun handleSearchWeatherResponse(response: Response<CityDataModel>) {
        if (response.isSuccessful) {
            val dataResponse = response?.body()
            if (dataResponse != null) {
                view.displaySearchResults(dataResponse)
            } else {
                view.displayError("Invalid Response")
            }
        } else {
            view.displayError("Failed to fetch error")
        }
    }

    override fun handleError() {
        view.displayError()
    }

    override fun handleError(reason: String) {
        view.displayError(reason)
    }

}