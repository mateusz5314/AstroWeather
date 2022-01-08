package mjaniak.astroweather.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import mjaniak.astroweather.R
import mjaniak.astroweather.WeatherDataProvider

class BasicWeatherData : Fragment() {
    private val LOG_TAG = "BasicWeatherData_FRAGMENT"
    private lateinit var mFragmentView : View
    private val mWeatherDataProvider = WeatherDataProvider.mObject
    private val mClockHandler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mFragmentView = inflater.inflate(R.layout.fragment_basic_weather_data, container, false)
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
        var outText : TextView = mFragmentView.findViewById(R.id.city)
        outText.text = String.format("%s %s", getString(R.string.city), mWeatherDataProvider.mBasicDailyWeatherData.mCity)
        outText = mFragmentView.findViewById(R.id.latitude)
        outText.text = String.format("%s: %f", getString(R.string.latitude), mWeatherDataProvider.mBasicDailyWeatherData.mLatitude)
        outText = mFragmentView.findViewById(R.id.longitude)
        outText.text = String.format("%s: %f", getString(R.string.longitude), mWeatherDataProvider.mBasicDailyWeatherData.mLongitude)
        outText = mFragmentView.findViewById(R.id.time)
        outText.text = String.format("%s %s", getString(R.string.current_time), mWeatherDataProvider.mBasicDailyWeatherData.mCurrentTime.toString())
        outText = mFragmentView.findViewById(R.id.temperature)
        outText.text = String.format("%s %f", getString(R.string.temperature), mWeatherDataProvider.mBasicDailyWeatherData.mTemperature)
        outText = mFragmentView.findViewById(R.id.pressure)
        outText.text = String.format("%s %d", getString(R.string.pressure), mWeatherDataProvider.mBasicDailyWeatherData.mPressure)
        outText = mFragmentView.findViewById(R.id.description)
        outText.text = String.format("%s %s", getString(R.string.weather), mWeatherDataProvider.mBasicDailyWeatherData.mDescription)
        outText = mFragmentView.findViewById(R.id.visualization)
        outText.text = String.format("%s", mWeatherDataProvider.mBasicDailyWeatherData.mVisualization)

        val linearLayout: LinearLayout = mFragmentView.findViewById(R.id.basicWeatherLayout)
        if (linearLayout.findViewWithTag<TextView>("connectionWarning") != null) {
            if (!mWeatherDataProvider.mOutdatedData) {
                linearLayout.removeView(linearLayout.findViewWithTag<TextView>("connectionWarning"))
            }
        }
        else {
            if (mWeatherDataProvider.mOutdatedData) {
                val warning = TextView(activity)
                warning.text = getString(R.string.connectionWarning)
                warning.tag = "connectionWarning"
                linearLayout.addView(warning)
            }
        }
    }
}