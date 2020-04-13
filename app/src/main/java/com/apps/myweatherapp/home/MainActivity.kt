package com.apps.myweatherapp.home

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.apps.myweatherapp.R
import com.apps.myweatherapp.location.view.LocationFragment
import com.apps.myweatherapp.search.view.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    lateinit var navigation: BottomNavigationView
    lateinit var windowView: Window
    private val fm = supportFragmentManager
    val searchFragment: Fragment =
        SearchFragment()
    val locationFragment: Fragment =
        LocationFragment()
    var active: Fragment = locationFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        windowView = this.window
        navigation = findViewById(R.id.bottom_navigation)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        fm.beginTransaction().add(R.id.frame_container, searchFragment, "1").hide(searchFragment)
            .commit();
        fm.beginTransaction().add(R.id.frame_container, locationFragment, "2").commit();
        //loadFragment(SearchFragment())

    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            var fragment: Fragment
            when (item.itemId) {
                R.id.action_search -> {


                    fragment = SearchFragment()
                    //loadFragment(fragment)
                    fm.beginTransaction().hide(active).show(searchFragment).commit();
                    active = searchFragment
                    navigation.setBackgroundColor(
                        ContextCompat.getColor(
                            this,
                            R.color.status_bar_blue
                        )
                    )
                    windowView.statusBarColor = (ContextCompat.getColor(
                        this,
                        R.color.status_bar_grey
                    ))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.action_location -> {

                    fragment =
                        LocationFragment()
                    //loadFragment(fragment)
                    fm.beginTransaction().hide(active).show(locationFragment).commit();
                    active = locationFragment
                    navigation.setBackgroundColor(
                        ContextCompat.getColor(
                            this,
                            R.color.status_bar_blue
                        )
                    )
                    windowView.statusBarColor = (ContextCompat.getColor(
                        this,
                        R.color.status_bar_grey
                    ))
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


}
