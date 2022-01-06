package mjaniak.astroweather

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup

class Settings : AppCompatActivity() {
    private val LOG_TAG : String = "SettingsActivity"

    enum class TemperatureUnits {
        C,
        F
    }

    companion object Values{
        var mRefreshTime : Long = 2000L
        var mLatitude : Double = 51.75
        var mLongitude : Double = 19.46667
        var mCity : String = "Lodz"
        var mTemperatureUnits : TemperatureUnits = TemperatureUnits.C
        var mRefreshWeather : Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        var view = findViewById<EditText>(R.id.inpRefresh)
        view.hint = mRefreshTime.toString()
        view = findViewById(R.id.inp_latitude)
        view.hint = mLatitude.toString()
        view = findViewById(R.id.inp_longitude)
        view.hint = mLongitude.toString()

        view = findViewById(R.id.inp_city)
        view.hint = mCity

        val selectedTemperatureId = if (mTemperatureUnits == TemperatureUnits.C)
            R.id.temperatureUnit_C else R.id.temperatureUnit_F
        val radioView = findViewById<RadioButton>(selectedTemperatureId)
        radioView.isChecked = true
    }

    fun onSaveBtn(view: android.view.View) {
        Log.i(LOG_TAG, "save click!")

        val newRefreshTime = findViewById<EditText>(R.id.inpRefresh).text
        if (newRefreshTime.isNotEmpty()) {
            mRefreshTime = newRefreshTime.toString().toLong()
            Log.i(LOG_TAG, "mRefreshTime: $mRefreshTime")
        }
        val newLatitude = findViewById<EditText>(R.id.inp_latitude).text
        if (newLatitude.isNotEmpty()) {
            mLatitude = newLatitude.toString().toDouble()
            Log.i(LOG_TAG, "mLatitude: $mLatitude")
        }
        val newLongitude = findViewById<EditText>(R.id.inp_longitude).text
        if (newLongitude.isNotEmpty()) {
            mLongitude = newLongitude.toString().toDouble()
            Log.i(LOG_TAG, "mLongitude: $mLongitude")
        }

        finish()
    }

    fun weatherUpdated() {
        mRefreshWeather = false
    }

    private fun checkWeatherSettings() {
        val newCity = findViewById<EditText>(R.id.inp_city).text
        if (newCity.isNotEmpty()) {
            mCity = newCity.toString()
            Log.i(LOG_TAG, "mCity: $mCity")
        }
        when (findViewById<RadioGroup>(R.id.temperatureUnitSelect).checkedRadioButtonId) {
            R.id.temperatureUnit_C -> mTemperatureUnits = TemperatureUnits.C
            R.id.temperatureUnit_F -> mTemperatureUnits = TemperatureUnits.F
            else -> {
                // Nothing to do.
                Log.d(LOG_TAG, "Temperature unit not changed.")
            }
        }
    }

    fun onSaveWeatherBtn(view: android.view.View) {
        Log.i(LOG_TAG, "save weather click!")

        checkWeatherSettings()

        finish()
    }

    fun onSaveAndRefWeatherBtn(view: android.view.View) {
        Log.i(LOG_TAG, "save&refresh weather click!")

        checkWeatherSettings()
        mRefreshWeather = true

        finish()
    }
}