package com.shon.projects.payappmodel.utils.glide.networking


import com.apps.myweatherapp.location.model.WeatherModel
import com.apps.myweatherapp.search.model.CityDataModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiInterface {

    @GET("forecast")
    fun getData(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("units") units: String
    ): Call<WeatherModel>

    @GET("weather")
    fun getCityData(
        @Query("q") city: String,
        @Query("units") units: String
    ): Call<CityDataModel>

}