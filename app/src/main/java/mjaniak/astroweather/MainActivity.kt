package mjaniak.astroweather

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import android.view.LayoutInflater
import android.view.View
import mjaniak.astroweather.fragments.*
import android.app.Activity
import android.content.Context
import java.io.File


class MainActivity : FragmentActivity() {

    private val LOG_TAG : String = "MainActivity"
    private lateinit var mViewPager: ViewPager2
    private var mUsePager : Boolean = false
    private var mMainLayout : String = ""
    private var mNumOfPagesInPager = 2

    companion object InternalStorage {
        lateinit var mDir: File
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        try {
            mViewPager = findViewById(R.id.pager)
            val pagerAdapter = ScreenSlidePagerAdapter(this)
            mViewPager.adapter = pagerAdapter
            mUsePager = true;
        }
        catch (e : Exception)
        {
            Log.i(LOG_TAG, "Pager not found, tablet layout applied.")
        }

        val inflater = layoutInflater
        val view: View = inflater.inflate(R.layout.main_activity, null)
        mMainLayout = view.tag.toString()
        if (mMainLayout == resources.getString(R.string.phone_tag))
        {
            mNumOfPagesInPager = 5
        }

        mDir = File(this.filesDir, "AstroWeather")
        if (!mDir.exists()) {
            mDir.mkdir()
        }
    }

    override fun onBackPressed() {
        if (mUsePager) {
            if (mViewPager.currentItem == 0) {
                // If the user is currently looking at the first step, allow the system to handle the
                // Back button. This calls finish() on this activity and pops the back stack.
                super.onBackPressed()
            } else {
                // Otherwise, select the previous step.
                mViewPager.currentItem = mViewPager.currentItem - 1
            }
        }
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = mNumOfPagesInPager

        override fun createFragment(position: Int) : Fragment {
            Log.i(LOG_TAG, mMainLayout);
            when (mMainLayout) {
                resources.getString(R.string.phone_tag) -> {
                    when (position) {
                        0 -> return Sun()
                        1 -> return Moon()
                        2 -> return BasicWeatherData()
                        3 -> return ExtraWeatherData()
                        4 -> return ForecastWeather()
                        else -> { // Note the block
                            print("Incorrect slide.")
                        }
                    }
                }
                resources.getString(R.string.tablet_tag) -> {
                    when (position) {
                        0 -> return GroupMoonSun()
                        1 -> return GroupWeather()
                        else -> { // Note the block
                            print("Incorrect slide.")
                        }
                    }
                }
            }

            return IncorrectSlide()
        }
    }

    fun onMenuClick(view: android.view.View) {
        Log.i(LOG_TAG, "menu click!")
        val intent = Intent(this, Settings::class.java)
        startActivity(intent)
    }
}