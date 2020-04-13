package com.apps.myweatherapp.location.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apps.myweatherapp.R
import com.apps.myweatherapp.location.model.Weather
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class DayListAdapter(
    var dataList: ArrayList<Weather>,
    private val context: Context
) : RecyclerView.Adapter<DayListAdapter.ViewHolder>() {

    companion object {
        private val URL_IMG: String = "http://openweathermap.org/img/wn/"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.day_list_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.dayTxt.text = dayExtractor(data.timestamp)
        holder.minTxt.text = data.temperature.maxTemp.roundToInt().toString() + "\u00B0"
        holder.maxTxt.text = data.temperature.minTemp.roundToInt().toString() + "\u00B0"
        Glide
            .with(context)
            .load(URL_IMG + data.description[0].icon + "@2x.png")
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .into(holder.image)

    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dayTxt: TextView = itemView.findViewById(R.id.day_txt)
        val minTxt: TextView = itemView.findViewById(R.id.min_txt)
        val maxTxt: TextView = itemView.findViewById(R.id.max_txt)
        val image: ImageView = itemView.findViewById(R.id.img)

    }


    private fun dayExtractor(timestamp: Long): String {
        val formatter = SimpleDateFormat("EEEE")
        formatter.timeZone = TimeZone.getTimeZone("GMT")
        return formatter.format(timestamp * 1000).toString()
    }

}