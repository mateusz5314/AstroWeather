package mjaniak.astroweather

class ForecastWeatherData {
    val NUMBER_OF_DAYS = 7
    var mDaily: List<BasicDailyWeatherData> = List(NUMBER_OF_DAYS) { BasicDailyWeatherData() }
}