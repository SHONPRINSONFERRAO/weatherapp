package com.apps.myweatherapp.search.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apps.myweatherapp.R
import com.apps.myweatherapp.search.model.CityDataModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlin.math.roundToInt

class CityAdapter(
    val dataList: ArrayList<CityDataModel>,
    val context: Context
) : RecyclerView.Adapter<CityAdapter.ViewHolder>() {

    companion object {
        private val URL_IMG: String = "http://openweathermap.org/img/wn/"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.city_list_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.cityName.text = data.city
        holder.tempTxt.text = data?.temperature?.tempMax.roundToInt()
            .toString() + "\u00B0" + "/ " + data?.temperature?.tempMin.roundToInt()
            .toString() + "\u00B0"
        holder.speedTxt.text = "Speed: " + data?.wind?.speed
        Glide
            .with(context)
            .load(URL_IMG + data.weather[0].icon + "@2x.png")
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .into(holder.image)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cityName: TextView = itemView.findViewById(R.id.city_txt)
        val image: ImageView = itemView.findViewById(R.id.image)
        val speedTxt: TextView = itemView.findViewById(R.id.speed_txt)
        val tempTxt: TextView = itemView.findViewById(R.id.temp_txt)


    }

}