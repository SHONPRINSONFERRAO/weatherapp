package com.apps.myweatherapp.location.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WeatherModel(
    @Expose
    @SerializedName("list")
    val weather: ArrayList<Weather>,
    @Expose
    @SerializedName("city")
    val city: City
)

data class City(
    @Expose
    @SerializedName("name")
    val name: String
) {

}

data class Weather(
    @Expose
    @SerializedName("main")
    val temperature: Temperature,
    @Expose
    @SerializedName("weather")
    val description: ArrayList<Description>,
    @Expose
    @SerializedName("dt")
    val timestamp: Long,
    @Expose
    @SerializedName("dt_txt")
    val dateTxt: String
)

data class Temperature(
    @Expose
    @SerializedName("feels_like")
    val predictionTemp: Float,
    @Expose
    @SerializedName("temp_min")
    val minTemp: Float,

    @Expose
    @SerializedName("temp_max")
    val maxTemp: Float,
    @Expose
    @SerializedName("temp")
    val temperature: Float,
    @Expose
    @SerializedName("humidity")
    val humidity: Float
)

data class Description(
    @Expose
    @SerializedName("description")
    val description: String,
    @Expose
    @SerializedName("main")
    val mainDescription: String,
    @Expose
    @SerializedName("icon")
    val icon: String
)

