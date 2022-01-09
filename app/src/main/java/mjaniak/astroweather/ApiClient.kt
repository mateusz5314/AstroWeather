package mjaniak.astroweather

import android.util.Log
import okhttp3.*
import java.io.IOException
import kotlin.reflect.KFunction1

class ApiClient {
    private val LOG_TAG = "ApiClient"
    private val mClient = OkHttpClient()
    private val API_KEY = "dadcc5d9538deae82b92f37a06c733b2"
    private val DESTINATION = "https://api.openweathermap.org/data/2.5/"

    fun requestData(city: String, callback: KFunction1<String, Unit>) {
        Log.i(LOG_TAG, "Requesting data")
        val requestText = DESTINATION.plus("weather?q=").plus(city).plus("&appid=").plus(API_KEY)
        currentDataReq(requestText, callback)
    }

    fun requestForecast(lat: Double, long: Double, callback: KFunction1<String, Unit>) {
        Log.i(LOG_TAG, "Requesting data")
        val requestText = DESTINATION.plus("onecall?lat=").plus(lat.toString()).plus("&lon=")
            .plus(long.toString()).plus("&exclude=hourly,minutely,alerts&appid=").plus(API_KEY)
        currentDataReq(requestText, callback)
    }

    private fun currentDataReq(destination: String, callback: KFunction1<String, Unit>) {
        val request = Request.Builder()
            .url(destination)
            .build()

        mClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(LOG_TAG, "onFailure: ".plus(e.toString()))
                callback("")
            }
            override fun onResponse(call: Call, response: Response) {
                Log.i(LOG_TAG, "onResponse")
                response.body()?.string()?.let {
                    if (it.contains("city not found")) {
                        callback("")
                    } else {
                        callback(it)
                    }}
            }
        })
    }
}