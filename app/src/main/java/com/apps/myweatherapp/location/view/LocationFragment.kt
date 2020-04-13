package com.apps.myweatherapp.location.view

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apps.myweatherapp.R
import com.apps.myweatherapp.location.model.Weather
import com.apps.myweatherapp.location.model.WeatherModel
import com.apps.myweatherapp.location.presenter.WeatherPresenterImpl
import com.apps.myweatherapp.location.presenter.WeatherViewContract
import com.apps.myweatherapp.network.NetworkRepository
import com.bumptech.glide.Glide
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import com.shon.projects.payappmodel.utils.glide.networking.ApiClient
import retrofit2.Response
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt


class LocationFragment : Fragment(), WeatherViewContract {


    private var mLocation: Location? = null

    private var param1: String? = null
    private var param2: String? = null

    companion object {
        private const val URL_IMG: String = "http://openweathermap.org/img/wn/"
    }

    private lateinit var dayList: RecyclerView
    private lateinit var variationList: RecyclerView
    private lateinit var dayListAdapter: DayListAdapter
    private lateinit var variationListAdapter: VariationListAdapter
    private var dataList: ArrayList<Weather> = ArrayList()
    private var filteredList: ArrayList<Weather> = ArrayList()
    private var filteredTempList: ArrayList<Weather> = ArrayList()
    private lateinit var temp_city_txt: TextView
    private lateinit var climate_city_txt: TextView
    private lateinit var img: ImageView
    private lateinit var minTxt: TextView
    private lateinit var maxTxt: TextView
    private lateinit var dayTxt: TextView
    private lateinit var tempTxt: TextView
    private lateinit var cityTxt: TextView

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var mContext: Context


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext)
        getLastLocation()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_location, container, false)
        temp_city_txt = view.findViewById(R.id.temp_city_txt)
        climate_city_txt = view.findViewById(R.id.climate_city_txt)
        dayList = view.findViewById(R.id.day_list)
        variationList = view.findViewById(R.id.variation_list)
        dayTxt = view.findViewById(R.id.day_txt)
        img = view.findViewById(R.id.img)
        minTxt = view.findViewById(R.id.min_txt)
        maxTxt = view.findViewById(R.id.max_txt)
        tempTxt = view.findViewById(R.id.temp_txt)
        cityTxt = view.findViewById(R.id.city_txt)


        setUpDayList()
        setUpVariationList()

        return view
    }


    private fun setUpVariationList() {
        variationList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        variationListAdapter =
            VariationListAdapter(
                filteredTempList,
                mContext
            )
        variationList.adapter = variationListAdapter
        var dividerItemDecoration = DividerItemDecoration(
            context,
            DividerItemDecoration.VERTICAL
        )
        val myIcon = ContextCompat.getDrawable(mContext, R.drawable.divider)

        if (myIcon != null) {
            dividerItemDecoration.setDrawable(myIcon)
        }
        variationList.addItemDecoration(dividerItemDecoration)
    }

    private fun setUpDayList() {
        dayList.layoutManager = LinearLayoutManager(context)
        dayListAdapter =
            DayListAdapter(
                filteredList,
                mContext
            )
        dayList.adapter = dayListAdapter
        var dividerItemDecoration = DividerItemDecoration(
            context,
            DividerItemDecoration.VERTICAL
        )
        val myIcon = ContextCompat.getDrawable(mContext, R.drawable.divider)

        if (myIcon != null) {
            dividerItemDecoration.setDrawable(myIcon)
        }
        dayList.addItemDecoration(dividerItemDecoration)
    }

    override fun onResume() {
        super.onResume()
    }

    private fun loadWeatherData(lat: Double?, lon: Double?) {
        dataList.clear()

        val repository = NetworkRepository(ApiClient)
        val presenter = WeatherPresenterImpl(this, repository)

        presenter.fetchWeather(lat, lon)

    }


    private fun dateCalc(dataList: ArrayList<Weather>) {
        filteredList.clear()
        var current: String = dataList[0].dateTxt.substring(0, 10)
        for (i in 0 until dataList.size) {

            val tempDate = dataList[i].dateTxt.substring(0, 10)
            Log.i("DATE", tempDate)

            if (tempDate != current) {
                filteredList.add(dataList[i])
                current = tempDate
            }
        }
        dayList.adapter?.notifyDataSetChanged()
        Log.i("size", filteredList.size.toString())
    }

    private fun timeCalc(dataList: ArrayList<Weather>) {
        filteredTempList.clear()
        for (items in 0 until 10) {
            filteredTempList.add(dataList[items])
        }
        variationList.adapter?.notifyDataSetChanged()
        Log.i("DATAS", filteredTempList.toString())
    }

    private fun dayExtractor(timestamp: Long): String {
        val formatter = SimpleDateFormat("EEEE")
        formatter.timeZone = TimeZone.getTimeZone("GMT")
        return formatter.format(timestamp * 1000).toString()
    }

    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                fusedLocationClient.lastLocation.addOnCompleteListener { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        if (dataList.isEmpty()) {
                            loadWeatherData(
                                location?.latitude,
                                location?.longitude
                            )
                        }
                    }
                }
            } else {
                Toast.makeText(mContext, "Turn on location", Toast.LENGTH_LONG).show()
                buildAlertMessageNoGps()
            }
        } else {
            requestPermissions()
        }
    }


    private fun checkPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            mContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    var PERMISSION_ID = 44

    private fun requestPermissions() {
        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }


    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions!!, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            }
        }
    }

    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext)
        fusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            if (dataList.isEmpty()) {
                loadWeatherData(
                    mLastLocation?.latitude,
                    mLastLocation?.longitude
                )
            }
        }
    }


    private fun buildAlertMessageNoGps() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(mContext)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton(
                "Yes"
            ) { dialog, _ ->
                startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 201)
                dialog.cancel()
            }
            .setNegativeButton(
                "No"
            ) { dialog, _ -> dialog.cancel() }
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            201 -> {
                getLastLocation()
            }
        }
    }

    override fun displayWeatherResults(response: WeatherModel) {
        val responseData = response?.weather
        responseData?.let { dataList.addAll(it) }
        if (!responseData.isNullOrEmpty()) {
            dateCalc(dataList)
            timeCalc(dataList)
        }
        //dayList.adapter?.notifyDataSetChanged()
        val dashboardData = dataList[0]
        temp_city_txt.text =
            dashboardData.temperature.temperature.roundToInt().toString() + "\u00B0"
        climate_city_txt.text = dashboardData.description[0].mainDescription
        tempTxt.text = dashboardData.temperature.maxTemp.roundToInt().toString() + "/" +
                dashboardData.temperature.minTemp.roundToInt() + "\u00B0"


        cityTxt.text = response?.city?.name

        Glide
            .with(this@LocationFragment)
            .load(URL_IMG + dashboardData.description[0].icon + "@2x.png")
            .centerCrop()
            .into(img)

        dayTxt.text = dayExtractor(dashboardData.timestamp)
        minTxt.text = dashboardData.temperature.maxTemp.roundToInt().toString() + "\u00B0"
        maxTxt.text = dashboardData.temperature.minTemp.roundToInt().toString() + "\u00B0"
    }



    override fun displayError() {
        view?.rootView?.let {
            Snackbar.make(
                it,
                "Error loading data from Repository",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }


    override fun displayError(s: String?) {
        view?.rootView?.let { Snackbar.make(it, s.toString(), Snackbar.LENGTH_SHORT).show() }
    }
}
