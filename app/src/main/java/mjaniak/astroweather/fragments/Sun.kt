package mjaniak.astroweather.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import mjaniak.astroweather.R
import com.astrocalculator.AstroCalculator
import com.astrocalculator.AstroDateTime
import mjaniak.astroweather.Settings
import java.util.*

class Sun : Fragment() {
    private val LOG_TAG = "SUN_FRAGMENT"
    private val mMainHandler = Handler(Looper.getMainLooper())
    private val mClockHandler = Handler(Looper.getMainLooper())
    private lateinit var mFragmentView : View
    private var mUpdateDelay : Long = 0
    private var mLatitude : Double = 0.0
    private var mLongitude : Double = 0.0
    private var mAzimuthRise : Double = 0.0
    private var mAzimuthSet : Double = 0.0
    private lateinit var mSunriseTime : AstroDateTime
    private lateinit var mSunsetTime : AstroDateTime
    private lateinit var mCivilTwilightTime : AstroDateTime
    private lateinit var mMorningCivilTwilightTime : AstroDateTime

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupTimers()
        mFragmentView = inflater.inflate(R.layout.fragment_sun, container, false)
        return mFragmentView
    }

    override fun onDetach() {
        super.onDetach()
        mMainHandler.removeCallbacksAndMessages(null)
        mClockHandler.removeCallbacksAndMessages(null)
    }

    private fun setupTimers() {
        mMainHandler.post(object : Runnable {
            override fun run() {
                updateSunData()
                mMainHandler.postDelayed(this, mUpdateDelay)
            }
        })
        mClockHandler.post(object : Runnable {
            override fun run() {
                updateTime()
                mMainHandler.postDelayed(this, 1000)
            }
        })
    }

    private fun updateTime() {
        Log.i(LOG_TAG, "update sun current time")
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Poland"))

        var outText : TextView = mFragmentView.findViewById(R.id.sun_cur_time)
        var text = String.format("%s %02d:%02d:%02d", getString(R.string.current_time), calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND))
        outText.text = text

        outText = mFragmentView.findViewById(R.id.sun_cur_loc)
        text = String.format("%s \nLatitude: %f \nLongitude: %f", getString(R.string.location), mLatitude, mLongitude)
        outText.text = text

        if (Settings.mRefreshTime != mUpdateDelay)
        {
            mMainHandler.removeCallbacksAndMessages(null)
            mClockHandler.removeCallbacksAndMessages(null)
            mUpdateDelay = Settings.mRefreshTime
            setupTimers()
        }
        mLatitude = Settings.mLatitude
        mLongitude = Settings.mLongitude
    }

    private fun updateSunData() {
        Log.i(LOG_TAG, "update sun data")
        readNewData()
        var outText : TextView = mFragmentView.findViewById(R.id.sunrise)
        var text = String.format("%s %02d:%02d:%02d", getString(R.string.sunrise), mSunriseTime.hour,
            mSunriseTime.minute,  mSunriseTime.second)
        outText.text = text
        outText = mFragmentView.findViewById(R.id.sunset)
        text = String.format("%s %02d:%02d:%02d", getString(R.string.sunset), mSunsetTime.hour,
            mSunsetTime.minute,  mSunsetTime.second)
        outText.text = text
        outText = mFragmentView.findViewById(R.id.civiltwilight)
        text = String.format("%s %02d:%02d:%02d", getString(R.string.sunset), mCivilTwilightTime.hour,
            mCivilTwilightTime.minute,  mCivilTwilightTime.second)
        outText.text = text
        outText = mFragmentView.findViewById(R.id.civilmorningtwilight)
        text = String.format("%s %02d:%02d:%02d", getString(R.string.sunset), mMorningCivilTwilightTime.hour,
            mMorningCivilTwilightTime.minute,  mMorningCivilTwilightTime.second)
        outText.text = text
        outText = mFragmentView.findViewById(R.id.azimuthRise)
        text = getString(R.string.azimuth_rise) + mAzimuthRise.toString()
        outText.text = text
        outText = mFragmentView.findViewById(R.id.azimuthSet)
        text = getString(R.string.azimuth_set) + mAzimuthSet.toString()
        outText.text = text
    }

    private fun readNewData() {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Poland"))
        val dateTime = AstroDateTime(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND),
            calendar.get(Calendar.DST_OFFSET) / 3600000, calendar.timeZone.useDaylightTime())
        val location = AstroCalculator.Location(mLatitude, mLongitude)
        val sunInfo = AstroCalculator(dateTime, location).sunInfo

        Log.i(LOG_TAG, dateTime.toString())
        mSunriseTime = sunInfo.sunrise
        mSunsetTime = sunInfo.sunset
        mCivilTwilightTime = sunInfo.twilightEvening
        mMorningCivilTwilightTime = sunInfo.twilightMorning
        mAzimuthRise = sunInfo.azimuthRise
        mAzimuthSet = sunInfo.azimuthSet
    }
}