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
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


class VariationListAdapter(
    var variationList: ArrayList<Weather>,
    private val context: Context
) : RecyclerView.Adapter<VariationListAdapter.ViewHolder>() {

    companion object {
        private val URL_IMG: String = "http://openweathermap.org/img/wn/"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.variation_list_row, parent, false)
        return ViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return variationList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = variationList[position]
        holder.timeTxt.text = timeExtractor(data.timestamp)
        holder.temperature.text = data.temperature.temperature.roundToInt().toString() + "\u00B0"
        Glide
            .with(context)
            .load(URL_IMG + data.description[0].icon + "@2x.png")
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.image)


    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timeTxt: TextView = itemView.findViewById(R.id.time_txt)
        val temperature: TextView = itemView.findViewById(R.id.temperature)
        val image: ImageView = itemView.findViewById(R.id.image)

    }


    private fun timeExtractor(timestamp: Long): String {
        val dateFormat: DateFormat = SimpleDateFormat("hh:mm aa")
        dateFormat.timeZone = TimeZone.getTimeZone("GMT")
        return dateFormat.format(timestamp * 1000).toString()
    }
}

