package mk.finki.mpip.weatherlens.viewmodels

import mk.finki.mpip.weatherlens.R
import mk.finki.mpip.weatherlens.network.models.WeatherResponse
import java.text.SimpleDateFormat
import java.util.*

class CreateHomeContentData {

  operator fun invoke(weatherData: WeatherResponse) =
    HomeViewState.WeatherContent(
      weather = weatherData.weather[0].main,
      weatherIcon = getWeatherIcon(weatherData.weather[0].icon),
      minTemperature = "${weatherData.main.temp_min} C",
      maxTemperature = "${weatherData.main.temp_max} C",
      sunrise = unixTime(weatherData.sys.sunrise),
      sunset = unixTime(weatherData.sys.sunset),
      humidity = weatherData.main.humidity.toString(),
      wind = weatherData.wind.speed.toString(),
      location = weatherData.sys.country,
      city = weatherData.name
    )

  private fun getWeatherIcon(icon: String): Int = when (icon) {
    "01d" -> R.drawable.ic_sunny
    "11d" -> R.drawable.ic_stormy
    "10d", "11n" -> R.drawable.ic_rainy
    "13d", "13n" -> R.drawable.ic_snowy
    else -> R.drawable.ic_cloudy
  }

  private fun unixTime(timex: Long): String {
    val date = Date(timex)
    val sdf = SimpleDateFormat("HH:mm")
    sdf.timeZone = TimeZone.getDefault()
    return sdf.format(date) ?: ""
  }
}