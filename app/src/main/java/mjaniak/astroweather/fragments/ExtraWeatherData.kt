package mjaniak.astroweather.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import mjaniak.astroweather.R
import mjaniak.astroweather.WeatherDataProvider

class ExtraWeatherData : Fragment() {
    private val LOG_TAG = "ExtraWeatherData_FRAGMENT"
    private lateinit var mFragmentView : View
    private val mWeatherDataProvider = WeatherDataProvider.mObject
    private val mClockHandler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mFragmentView = inflater.inflate(R.layout.fragment_extra_weather_data, container, false)
        setupTimers()
        return mFragmentView
    }

    override fun onDetach() {
        super.onDetach()
        mClockHandler.removeCallbacksAndMessages(null)
    }

    private fun setupTimers() {
        mClockHandler.post(object : Runnable {
            override fun run() {
                update()
                mClockHandler.postDelayed(this, 1000)
            }
        })
    }

    private fun update()
    {
        var outText : TextView = mFragmentView.findViewById(R.id.windStrength)
        outText.text = String.format("%s %f", getString(R.string.wind_strength), mWeatherDataProvider.mExtraDailyWeatherData.mWindStrength)
        outText = mFragmentView.findViewById(R.id.windDirection)
        outText.text = String.format("%s: %s", getString(R.string.wind_direction), mWeatherDataProvider.mExtraDailyWeatherData.mWindDirection)
        outText = mFragmentView.findViewById(R.id.humidity)
        outText.text = String.format("%s: %d", getString(R.string.humidity), mWeatherDataProvider.mExtraDailyWeatherData.mHumidity)
        outText = mFragmentView.findViewById(R.id.visibility)
        outText.text = String.format("%s %s", getString(R.string.visibility), mWeatherDataProvider.mExtraDailyWeatherData.mVisibility)
    }
}