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
import java.util.*

class Sun : Fragment() {
    private val LOG_TAG = "SUN_FRAGMENT"
    private val mMainHandler = Handler(Looper.getMainLooper())
    private lateinit var mFragmentView : View
    private var mUpdateDelay = 2000L
    private var mLatitude = 51.75
    private var mLongitude = 19.46667
    private lateinit var mSunriseTime : AstroDateTime
    private lateinit var mSunsetTime : AstroDateTime
    private lateinit var mCivilTwilightTime : AstroDateTime
    private lateinit var mMorningCivilTwilightTime : AstroDateTime

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mMainHandler.post(object : Runnable {
            override fun run() {
                updateSunData()
                mMainHandler.postDelayed(this, mUpdateDelay)
            }
        })
        mFragmentView = inflater.inflate(R.layout.fragment_sun, container, false)
        return mFragmentView
    }

    private fun updateSunData() {
        Log.i(LOG_TAG, "update sun data")
        readNewData()
        var outText : TextView = mFragmentView.findViewById(R.id.sunrise)
        var text = getString(R.string.sunrise) + mSunriseTime.toString()
        outText.text = text
        outText = mFragmentView.findViewById(R.id.sunset)
        text = getString(R.string.sunset) + mSunsetTime.toString()
        outText.text = text
        outText = mFragmentView.findViewById(R.id.civiltwilight)
        text = getString(R.string.twilightE) + mCivilTwilightTime.toString()
        outText.text = text
        outText = mFragmentView.findViewById(R.id.civilmorningtwilight)
        text = getString(R.string.twilightM) + mMorningCivilTwilightTime.toString()
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
    }
}