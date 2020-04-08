package com.apps.myweatherapp.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apps.myweatherapp.R
import com.shon.projects.payappmodel.utils.glide.networking.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var mContext: Context
    private lateinit var itemsSearch: List<String>
    private var list: ArrayList<CityDataModel> = ArrayList()
    private lateinit var cityAdapter: CityAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    private lateinit var search: SearchView
    private lateinit var cityList: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_search, container, false)
        search = view.findViewById(R.id.searchView)
        cityList = view.findViewById(R.id.city_list)
        setUpSearch()
        setUpCityList()
        return view
    }

    private fun setUpCityList() {
        cityList.layoutManager = LinearLayoutManager(context)
        cityAdapter = CityAdapter(list, mContext)
        cityList.adapter = cityAdapter
    }

    private fun setUpSearch() {
        search.maxWidth = Integer.MAX_VALUE


        val searchComplete: SearchView.SearchAutoComplete =
            search.findViewById(R.id.search_src_text);
        searchComplete.setHintTextColor(resources.getColor(R.color.status_bar_grey))
        searchComplete.setTextColor(resources.getColor(android.R.color.white))
        search.setOnQueryTextListener(this)

    }

    override fun onQueryTextSubmit(arg0: String?): Boolean {
        when {
            itemsSearch.size > 7 -> {
                Toast.makeText(
                    mContext,
                    "Maximum 7 cities data can be requested",
                    Toast.LENGTH_SHORT
                ).show()
            }
            itemsSearch.size < 3 -> {
                Toast.makeText(
                    mContext,
                    "Minimum 3 cities data can be requested",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {
                list.clear()
                for (city in itemsSearch)
                    loadWeatherData(city.trim())
            }
        }
        Log.i("count", itemsSearch.toString())
        return false
    }

    override fun onQueryTextChange(arg0: String): Boolean {
        itemsSearch = arg0.split(",")
        if (itemsSearch.size > 7) {
            Toast.makeText(mContext, "Maximum 7 cities data can be requested", Toast.LENGTH_SHORT)
                .show()
        }
        //Log.i("count", itemsSearch.toString())
        return false
    }

    private fun loadWeatherData(city: String) {
        val call: Call<CityDataModel> = ApiClient.getClient.getCityData(city, "metric")

        call.enqueue(object : Callback<CityDataModel> {

            override fun onResponse(
                call: Call<CityDataModel>?,
                response: Response<CityDataModel>?
            ) {

                val dataResponse = response?.body()
                if (dataResponse != null) {
                    list.add(dataResponse)
                    cityList.adapter?.notifyDataSetChanged()
                }

            }

            override fun onFailure(call: Call<CityDataModel>?, t: Throwable?) {
                println(t?.localizedMessage + t?.cause + t?.message)
            }

        })
    }

}
