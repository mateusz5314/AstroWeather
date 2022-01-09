package mjaniak.astroweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import android.widget.ArrayAdapter
import mjaniak.astroweather.widgets.ExtendedSpinner

class Settings : AppCompatActivity() {
    private val LOG_TAG : String = "SettingsActivity"
    private val mFavCitiesFile = "FavCities.txt"
    private var mFavCities: ArrayAdapter<String>? = null

    enum class TemperatureUnits {
        C,
        F
    }

    companion object Values{
        var mRefreshTime : Long = 2000L
        var mLatitude : Double = 51.75
        var mLongitude : Double = 19.46667
        var mCity : String = "Lodz"
        var mTemperatureUnit : TemperatureUnits = TemperatureUnits.C
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

        val selectedTemperatureId = if (mTemperatureUnit == TemperatureUnits.C)
            R.id.temperatureUnit_C else R.id.temperatureUnit_F
        val radioView = findViewById<RadioButton>(selectedTemperatureId)
        radioView.isChecked = true


        val spinner: ExtendedSpinner = findViewById(R.id.favCities)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mCity = parent?.getItemAtPosition(position).toString()
                val cityDisplayed: TextView = findViewById(R.id.inp_city)
                cityDisplayed.text = mCity
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        val cities = getDataFromFile(mFavCitiesFile)
        if (cities.isEmpty()) {
            cities.add("")
        }
        val lst: ArrayList<String> = ArrayList(cities)
        mFavCities = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1, lst
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        if (mFavCities == null){
            Log.e(LOG_TAG, "mFavCities null")
        }
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

    private fun checkWeatherSettings() {
        val newCity = findViewById<EditText>(R.id.inp_city).text
        if (newCity.isNotEmpty()) {
            mCity = newCity.toString()
            Log.i(LOG_TAG, "mCity: $mCity")
        }
        when (findViewById<RadioGroup>(R.id.temperatureUnitSelect).checkedRadioButtonId) {
            R.id.temperatureUnit_C -> mTemperatureUnit = TemperatureUnits.C
            R.id.temperatureUnit_F -> mTemperatureUnit = TemperatureUnits.F
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

    fun onAddCityBtn(view: android.view.View) {
        Log.i(LOG_TAG, "add favourite city")
        var newCity = findViewById<TextView>(R.id.inp_city).text
        if (newCity.toString() != "") {
            var add = true
            for (i in 0 until mFavCities?.count!!) {
                if (newCity.toString() == mFavCities!!.getItem(i) as String) {
                    add = false
                    mFavCities!!.remove(mFavCities!!.getItem(i))
                    removeCityFromFile(newCity.toString())
                    newCity = ""
                    if (mFavCities!!.isEmpty) {
                        mFavCities!!.add("")
                    }
                    break
                }
            }
            if (add ) {
                mFavCities?.add(newCity.toString())
                saveDataToFile(mFavCitiesFile, newCity.toString())
            }
        }
    }

    fun onSaveAndRefWeatherBtn(view: android.view.View) {
        Log.i(LOG_TAG, "save&refresh weather click!")

        checkWeatherSettings()
        mRefreshWeather = true

        finish()
    }

    private fun getDataFromFile(fileName: String): MutableSet<String> {
        Log.i(LOG_TAG, "getDataFromFile: ".plus(fileName))
        val lineList = mutableSetOf<String>()
        try {
            val stream = File(MainActivity.mDir, fileName).inputStream()
            stream.bufferedReader().useLines { lines -> lines.forEach { lineList.add(it)} }

        } catch (e: Exception) {
            Log.e(LOG_TAG, "File not available")
        }
        return lineList
    }

    private fun saveDataToFile(fileName: String, content: String, append: Boolean=true) {
        Log.i(LOG_TAG, "saveDataToFile: ".plus(fileName))
        val file = File(MainActivity.mDir, fileName)
        file.createNewFile()

        val oFile = FileOutputStream(file, append)
        oFile.write(content.plus("\n").toByteArray())
    }

    private fun saveDataToFile(fileName: String, content: Set<String>, append: Boolean=true) {
        Log.i(LOG_TAG, "saveDataToFile: ".plus(fileName))
        val file = File(MainActivity.mDir, fileName)
        file.createNewFile()

        val oFile = FileOutputStream(file, append)
        for (line in content)
        {
            oFile.write(line.plus("\n").toByteArray())
        }
    }

    private fun removeCityFromFile(city: String) {
        val data = getDataFromFile(mFavCitiesFile)
        data.remove(city)
        saveDataToFile(mFavCitiesFile, data, false)
    }
}