package mjaniak.astroweather

import android.os.Handler
import android.os.Looper
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.sql.Time

class WeatherDataProvider {
    private val LOG_TAG = "WeatherDataProvider"
    var mBasicDailyWeatherData = BasicDailyWeatherData()
    var mExtraDailyWeatherData = ExtraDailyWeatherData()
    var mForecastWeatherData = ForecastWeatherData()
    var mOutdatedData = false
    private val mMainHandler = Handler(Looper.getMainLooper())
    private val mClockHandler = Handler(Looper.getMainLooper())
    private val mStorageFile : String = "LatestData.json"
    private val mStorageFileForecast : String = "LatestForecast.json"
    private var mUpdateDelay : Long = 900000
    private var mApiClient :ApiClient?
    private var mJsonReader = WeatherJsonReader()

    companion object Instance {
        val mObject: WeatherDataProvider = WeatherDataProvider()
    }

    init {
        setupTimers()

        mApiClient = try {
            ApiClient()
        } catch (e: Exception){
            Log.w(LOG_TAG, "Api Client could not be created.")
            null
        }
    }

    private fun setupTimers() {
        mClockHandler.post(object : Runnable {
            override fun run() {
                mBasicDailyWeatherData.mCurrentTime = Time(System.currentTimeMillis())
                mClockHandler.postDelayed(this, 1000)
                if (Settings.mRefreshWeather) {
                    Settings.mRefreshWeather = false
                    updateData()
                }
            }
        })
        mMainHandler.post(object : Runnable {
            override fun run() {
                updateData()
                mMainHandler.postDelayed(this, mUpdateDelay)
            }
        })
    }

    private fun requestResult(answer: String) {
        Log.i(LOG_TAG, "requestResult")
        Log.i(LOG_TAG, answer)

        if (answer.isEmpty()) {
            val data = getDataFromFile(mStorageFile)
            parseData(data)
            mOutdatedData = true
        }
        else {
            saveDataToFile(mStorageFile, answer)
            parseData(answer)
            mOutdatedData = false
        }
    }

    private fun requestForecastResult(answer: String) {
        Log.i(LOG_TAG, "requestForecastResult")
        Log.i(LOG_TAG, answer)

        if (answer.isEmpty()) {
            val data = getDataFromFile(mStorageFileForecast)
            parseForecastData(data)
        }
        else {
            saveDataToFile(mStorageFileForecast, answer)
            parseForecastData(answer)
        }
    }

    private fun updateData() {
        Log.i(LOG_TAG, "update data for: ".plus(Settings.mCity))
        mApiClient?.requestData(Settings.mCity, ::requestResult) ?: requestResult("")
    }

    private fun updateForecast() {
        mApiClient?.requestForecast(mBasicDailyWeatherData.mLatitude, mBasicDailyWeatherData.mLongitude,
            ::requestForecastResult) ?: requestForecastResult("")
    }

    private fun getDataFromFile(fileName: String): String {
        Log.i(LOG_TAG, "getDataFromFile: ".plus(fileName))
        var data = ""
        data = try {
            File(MainActivity.mDir, fileName).inputStream().readBytes().toString(Charsets.UTF_8)
        } catch (e: Exception) {
            Log.e(LOG_TAG, "File not available")
            ""
        }
        Log.i(LOG_TAG, "inputStream: ".plus(data))
        return data
    }

    private fun saveDataToFile(fileName: String, jsonMsg: String) {
        Log.i(LOG_TAG, "saveDataToFile: ".plus(fileName))
        val file = File(MainActivity.mDir, fileName)
        file.createNewFile()

        val oFile = FileOutputStream(file, false)
        oFile.write(jsonMsg.toByteArray())
    }

    private fun parseData(jsonMsg: String) {
        Log.i(LOG_TAG, "parseData")
        val (basic, extra) = mJsonReader.deserializeDailyData(jsonMsg)
        mBasicDailyWeatherData = basic
        mBasicDailyWeatherData.mCity = Settings.mCity
        if (Settings.mTemperatureUnit == Settings.TemperatureUnits.C) {
            mBasicDailyWeatherData.mTemperature -= 273.15
        }
        else if (Settings.mTemperatureUnit == Settings.TemperatureUnits.F) {
            mBasicDailyWeatherData.mTemperature = (mBasicDailyWeatherData.mTemperature - 273.15) * 1.8 + 32
        }
        mExtraDailyWeatherData = extra
        updateForecast()
    }

    private fun parseForecastData(jsonMsg: String) {
        Log.i(LOG_TAG, "parseForecastData")
        mForecastWeatherData = mJsonReader.deserializeForecastData(jsonMsg)
        for (day in mForecastWeatherData.mDaily) {
            if (Settings.mTemperatureUnit == Settings.TemperatureUnits.C) {
                day.mTemperature -= 273.15
            }
            else if (Settings.mTemperatureUnit == Settings.TemperatureUnits.F) {
                day.mTemperature = (day.mTemperature - 273.15) * 1.8 + 32
            }
        }
    }
}