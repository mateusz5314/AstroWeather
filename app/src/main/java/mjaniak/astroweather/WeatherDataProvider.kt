package mjaniak.astroweather

import android.os.Handler
import android.os.Looper
import java.sql.Time

class WeatherDataProvider() {
    var mBasicDailyWeatherData = BasicDailyWeatherData()
    var mExtraDailyWeatherData = ExtraDailyWeatherData()
    var mForecastWeatherData = ForecastWeatherData()
    private val mMainHandler = Handler(Looper.getMainLooper())
    private val mClockHandler = Handler(Looper.getMainLooper())
    private var mUpdateDelay : Long = 2000

    companion object Instance {
        val mObject: WeatherDataProvider = WeatherDataProvider()
    }

    init {
        setupTimers()
    }

    private fun setupTimers() {
        mClockHandler.post(object : Runnable {
            override fun run() {
                mBasicDailyWeatherData.mCurrentTime = Time(System.currentTimeMillis())
                mClockHandler.postDelayed(this, 1000)
            }
        })
        mMainHandler.post(object : Runnable {
            override fun run() {
                updateData()
                mMainHandler.postDelayed(this, mUpdateDelay)
            }
        })
    }

    private fun updateData() {
        mBasicDailyWeatherData.mLatitude += 1
    }
}