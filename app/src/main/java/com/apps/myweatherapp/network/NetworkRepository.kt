package com.apps.myweatherapp.network

import com.apps.myweatherapp.location.model.WeatherModel
import com.apps.myweatherapp.search.model.CityDataModel
import com.apps.myweatherapp.search.presenter.SearchPresenterImpl
import com.shon.projects.payappmodel.utils.glide.networking.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NetworkRepository(val api: ApiClient)

{

    fun loadWeatherData(lat: Double?, lon: Double?, callback: RepositoryCallback)
    {
        val call: Call<WeatherModel> =
            ApiClient.getClient.getData(lat.toString(), lon.toString(), "metric")

        call.enqueue(object : Callback<WeatherModel> {

            override fun onResponse(
                call: Call<WeatherModel>?,
                response: Response<WeatherModel>?
            ) {
                if (response != null) {
                    callback.handleWeatherResponse(response)
                } else { callback.handleError("Error occured") }
            }

            override fun onFailure(call: Call<WeatherModel>?, t: Throwable?) {
                println(t?.localizedMessage + t?.cause + t?.message)
                callback.handleError()
            }

        })
    }

    fun search(city: String, callback: RepositoryCallback)
    {
        val call: Call<CityDataModel> = ApiClient.getClient.getCityData(city, "metric")

        call.enqueue(object : Callback<CityDataModel> {

            override fun onResponse(
                call: Call<CityDataModel>?,
                response: Response<CityDataModel>?
            ) {

                if (response != null) {
                    callback.handleSearchWeatherResponse(response)
                } else { callback.handleError("Error occured") }

            }

            override fun onFailure(call: Call<CityDataModel>?, t: Throwable?) {
                println(t?.localizedMessage + t?.cause + t?.message)
                callback.handleError("Failed to load Data")
            }

        })
    }

    fun loadCityData(cities: String, callback: RepositoryCallback) {
        val call: Call<CityDataModel> = ApiClient.getClient.getCityData(cities, "metric")

        call.enqueue(object : Callback<CityDataModel> {

            override fun onResponse(
                call: Call<CityDataModel>?,
                response: Response<CityDataModel>?
            ) {

                if (response != null) {
                    callback.handleSearchWeatherResponse(response)
                } else { callback.handleError("Error occured") }

            }

            override fun onFailure(call: Call<CityDataModel>?, t: Throwable?) {
                println(t?.localizedMessage + t?.cause + t?.message)
                callback.handleError("Failed to load Data")
            }

        })
    }

    interface RepositoryCallback {
        fun handleWeatherResponse(response: Response<WeatherModel>)
        fun handleSearchWeatherResponse(response: Response<CityDataModel>)
        fun handleError()
        fun handleError(reason: String)
    }
}