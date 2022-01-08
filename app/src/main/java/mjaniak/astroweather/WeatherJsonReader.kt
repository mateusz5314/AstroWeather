package mjaniak.astroweather

import android.util.Log
import kotlinx.serialization.*
import kotlinx.serialization.json.*

class WeatherJsonReader {
    private val LOG_TAG = "WeatherJsonReader"

    @Serializable
    data class Coord(val lon: Double, val lat: Double)

    @Serializable
    data class Weather(val main: String, val description: String)

    @Serializable
    data class Main(val temp: Double, val pressure: Int, val humidity: Int)

    @Serializable
    data class Wind(val speed: Double, val deg: Int)

    @Serializable
    data class WeatherData(val coord: Coord, val weather: List<Weather>, val main: Main,
                           val wind: Wind, val visibility: Int)

    @Serializable
    data class Temp(val day: Double)

    @Serializable
    data class Daily(val temp: Temp, val pressure: Int, val weather: List<Weather>)

    @Serializable
    data class ForecastData(val daily: List<Daily>)

    fun deserializeDailyData(json: String): Pair<BasicDailyWeatherData, ExtraDailyWeatherData>{
        val basicData = BasicDailyWeatherData()
        val extraData = ExtraDailyWeatherData()

        val decoder = Json { ignoreUnknownKeys = true }
        val data = decoder.decodeFromString<WeatherData>(json)

        basicData.mLatitude = data.coord.lat
        basicData.mLongitude = data.coord.lon
        basicData.mDescription = data.weather[0].main
        basicData.mVisualization = data.weather[0].description
        basicData.mPressure = data.main.pressure
        basicData.mTemperature = data.main.temp

        extraData.mHumidity = data.main.humidity
        extraData.mVisibility = data.visibility.toString()
        extraData.mWindDirection = data.wind.deg.toString()
        extraData.mWindStrength = data.wind.speed

        return Pair(basicData, extraData)
    }

    fun deserializeForecastData(json: String): ForecastWeatherData {
        val forecastData = ForecastWeatherData()

        val decoder = Json { ignoreUnknownKeys = true }
        val data = decoder.decodeFromString<ForecastData>(json)
        for (i in 0..6) {
            forecastData.mDaily[i].mTemperature = data.daily[i].temp.day
            forecastData.mDaily[i].mPressure = data.daily[i].pressure
            forecastData.mDaily[i].mDescription = data.daily[i].weather[0].main
        }
        return forecastData
    }
}