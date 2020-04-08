package com.apps.myweatherapp.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CityDataModel(
    @Expose
    @SerializedName("weather")
    val weather: ArrayList<Weather>,
    @Expose
    @SerializedName("main")
    val temperature: Temperature,
    @Expose
    @SerializedName("wind")
    val wind: Wind,
    @Expose
    @SerializedName("name")
    val city: String
)

data class Weather(
    @Expose
    @SerializedName("description")
    val weather: String,
    @Expose
    @SerializedName("icon")
    val icon: String
)


data class Temperature(
    @Expose
    @SerializedName("temp")
    val temp: Float,
    @Expose
    @SerializedName("temp_min")
    val tempMin: Float,
    @Expose
    @SerializedName("temp_max")
    val tempMax: Float
)


data class Wind(
    @Expose
    @SerializedName("speed")
    val speed: String
)