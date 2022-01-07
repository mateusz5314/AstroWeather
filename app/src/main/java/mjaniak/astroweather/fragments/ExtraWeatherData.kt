package mjaniak.astroweather.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mjaniak.astroweather.R
import mjaniak.astroweather.WeatherDataProvider

class ExtraWeatherData : Fragment() {
    private val LOG_TAG = "ExtraWeatherData_FRAGMENT"
    private lateinit var mFragmentView : View
    private val mWeatherDataProvider = WeatherDataProvider.mObject

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mFragmentView = inflater.inflate(R.layout.fragment_extra_weather_data, container, false)
        return mFragmentView
    }
}