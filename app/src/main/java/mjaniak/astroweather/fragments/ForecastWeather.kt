package mjaniak.astroweather.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mjaniak.astroweather.R
class ForecastWeather : Fragment() {
    private val LOG_TAG = "ForecastWeather_FRAGMENT"
    private lateinit var mFragmentView : View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mFragmentView = inflater.inflate(R.layout.fragment_forecast_weather, container, false)
        return mFragmentView
    }
}