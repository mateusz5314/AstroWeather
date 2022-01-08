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
import java.util.*

class ForecastWeather : Fragment() {
    private val LOG_TAG = "ForecastWeather_FRAGMENT"
    private lateinit var mFragmentView : View
    private val mWeatherDataProvider = WeatherDataProvider.mObject
    private val mClockHandler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mFragmentView = inflater.inflate(R.layout.fragment_forecast_weather, container, false)
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
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Poland"))
        val today = calendar.get(Calendar.DAY_OF_MONTH)
        val data = mWeatherDataProvider.mForecastWeatherData.mDaily
        var outText : TextView = mFragmentView.findViewById(R.id.today_1)
        outText.text = String.format("%d\n%d\n%s", today + 1, data[0].mTemperature.toInt(), data[0].mDescription)
        outText = mFragmentView.findViewById(R.id.today_2)
        outText.text = String.format("%d\n%d\n%s", today + 2, data[1].mTemperature.toInt(), data[1].mDescription)
        outText = mFragmentView.findViewById(R.id.today_3)
        outText.text = String.format("%d\n%d\n%s", today + 3, data[2].mTemperature.toInt(), data[2].mDescription)
        outText = mFragmentView.findViewById(R.id.today_4)
        outText.text = String.format("%d\n%d\n%s", today + 4, data[3].mTemperature.toInt(), data[3].mDescription)
        outText = mFragmentView.findViewById(R.id.today_5)
        outText.text = String.format("%d\n%d\n%s", today + 5, data[4].mTemperature.toInt(), data[4].mDescription)
        outText = mFragmentView.findViewById(R.id.today_6)
        outText.text = String.format("%d\n%d\n%s", today + 6, data[5].mTemperature.toInt(), data[5].mDescription)
    }
}