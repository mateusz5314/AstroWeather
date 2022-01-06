package mjaniak.astroweather.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mjaniak.astroweather.R

class BasicWeatherData : Fragment() {
    private val LOG_TAG = "BasicWeatherData_FRAGMENT"
    private lateinit var mFragmentView : View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mFragmentView = inflater.inflate(R.layout.fragment_basic_weather_data, container, false)
        return mFragmentView
    }
}