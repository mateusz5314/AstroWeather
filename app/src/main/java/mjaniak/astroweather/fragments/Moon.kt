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
import com.astrocalculator.AstroCalculator
import com.astrocalculator.AstroDateTime
import mjaniak.astroweather.R
import mjaniak.astroweather.Settings
import java.util.*

class Moon : Fragment() {
    private val LOG_TAG = "MOON_FRAGMENT"
    private val mMainHandler = Handler(Looper.getMainLooper())
    private val mClockHandler = Handler(Looper.getMainLooper())
    private lateinit var mFragmentView : View
    private var mUpdateDelay : Long = 0
    private var mLatitude : Double = 0.0
    private var mLongitude : Double = 0.0
    private var mIllumination : Double = 0.0
    private var mAge : Double = 0.0
    private lateinit var mMoonriseTime : AstroDateTime
    private lateinit var mMoonsetTime : AstroDateTime
    private lateinit var mNextNewMoon : AstroDateTime
    private lateinit var mNextFullMoon : AstroDateTime

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        setupTimers()
        mFragmentView = inflater.inflate(R.layout.fragment_moon, container, false)
        return mFragmentView
    }

    private fun setupTimers() {
        mMainHandler.post(object : Runnable {
            override fun run() {
                updateMoonData()
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

        var outText : TextView = mFragmentView.findViewById(R.id.moon_cur_time)
        var text = String.format("%s %02d:%02d:%02d", getString(R.string.current_time), calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND))
        outText.text = text

        outText = mFragmentView.findViewById(R.id.moon_cur_loc)
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

    override fun onDetach() {
        super.onDetach()
        mMainHandler.removeCallbacksAndMessages(null)
        mClockHandler.removeCallbacksAndMessages(null)
    }

    private fun updateMoonData() {
        Log.i(LOG_TAG, "update moon data")
        readNewData()
        var outText : TextView = mFragmentView.findViewById(R.id.moonrise)
        var text = String.format("%s %02d:%02d:%02d", getString(R.string.moonrise), mMoonriseTime.hour,
            mMoonriseTime.minute,  mMoonriseTime.second)
        outText.text = text
        outText = mFragmentView.findViewById(R.id.moonset)
        text = String.format("%s %02d:%02d:%02d", getString(R.string.moonset), mMoonriseTime.hour,
            mMoonriseTime.minute,  mMoonriseTime.second)
        outText.text = text
        outText = mFragmentView.findViewById(R.id.new_moon)
        text = String.format("%s %02d.%02d %02d:%02d:%02d", getString(R.string.new_moon), mNextNewMoon.day, mNextNewMoon.month,
            mNextNewMoon.hour, mNextNewMoon.minute,  mNextNewMoon.second)
        outText.text = text
        outText = mFragmentView.findViewById(R.id.full_moon)
        text = String.format("%s %02d.%02d %02d:%02d:%02d", getString(R.string.full_moon), mNextFullMoon.day, mNextFullMoon.month,
            mNextFullMoon.hour, mNextFullMoon.minute,  mNextFullMoon.second)
        outText.text = text
        outText = mFragmentView.findViewById(R.id.illumination)
        text = String.format("%s %d%s", getString(R.string.illumination), (mIllumination * 100).toInt(), "%")
        outText.text = text
        outText = mFragmentView.findViewById(R.id.age)
        text = String.format("%s %f", getString(R.string.age), mAge)
        outText.text = text
    }

    private fun readNewData() {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Poland"))
        val dateTime = AstroDateTime(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND),
            calendar.get(Calendar.DST_OFFSET) / 3600000, calendar.timeZone.useDaylightTime())
        val location = AstroCalculator.Location(mLatitude, mLongitude)
        val moonInfo = AstroCalculator(dateTime, location).moonInfo

        Log.i(LOG_TAG, dateTime.toString())
        mMoonriseTime = moonInfo.moonrise
        mMoonsetTime = moonInfo.moonset
        mNextNewMoon = moonInfo.nextNewMoon
        mNextFullMoon = moonInfo.nextFullMoon
        mIllumination = moonInfo.illumination
        mAge = moonInfo.age
    }
}