package mjaniak.astroweather

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import mjaniak.astroweather.fragments.IncorrectSlide
import mjaniak.astroweather.fragments.Moon
import mjaniak.astroweather.fragments.Sun

class MainActivity : FragmentActivity() {

    private val LOG_TAG : String = "MainActivity"
    private lateinit var mViewPager: ViewPager2
    private var mUsePager : Boolean = false;

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
        private val NUM_PAGES = 2
        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int) : Fragment {
            when (position) {
                0 -> return Sun()
                1 -> return Moon()
                else -> { // Note the block
                    print("Incorrect slide.")
                }
            }
            return IncorrectSlide()
        }
    }
}