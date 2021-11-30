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

class Sun : Fragment() {
    private val LOG_TAG = "SUN_FRAGMENT"
    private val mMainHandler = Handler(Looper.getMainLooper())
    private lateinit var mFragmentView : View
    private var mUpdateDelay = 2000L
    private var mSunriseTime = 0.0
    private var mSunsetTime = 1.0
    private var mCivilTwilightTime = 2.0
    private var mMorningCivilTwilightTime = 3.0

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
        text = getString(R.string.civiltwilight) + mCivilTwilightTime.toString()
        outText.text = text
        outText = mFragmentView.findViewById(R.id.civilmorningtwilight)
        text = getString(R.string.civilmorningtwilight) + mMorningCivilTwilightTime.toString()
        outText.text = text
    }

    private fun readNewData() {
        mSunriseTime++
        mSunsetTime++
        mCivilTwilightTime++
        mMorningCivilTwilightTime++
    }
}