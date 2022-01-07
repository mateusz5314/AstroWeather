package mjaniak.astroweather

import java.sql.Time

class BasicDailyWeatherData {
    var mCity: String = ""
    var mLatitude: Double = 0.0
    var mLongitude: Double = 0.0
    var mCurrentTime: Time = Time(0)
    var mTemperature: Int = 0
    var mPressure: Int = 0
    var mDescription: String = "{description}"
    var mVisualization: String = "{visualization}"
}