package mk.finki.mpip.weatherlens.viewmodels.home

import mk.finki.mpip.weatherlens.R
import mk.finki.mpip.weatherlens.network.models.WeatherResponse
import java.text.SimpleDateFormat
import java.util.*

class CreateHomeContentData {

  companion object {
    const val UNIT = "C"
    const val PERCENT = "%"
    const val METERS_PER_SECOND = "m/s"
  }

  operator fun invoke(weatherData: WeatherResponse) =
    HomeViewState.WeatherContent(
      weather = weatherData.weather[0].main,
      weatherIcon = getWeatherIcon(weatherData.weather[0].icon),
      minTemperature = "${weatherData.main.temp_min} $UNIT",
      maxTemperature = "${weatherData.main.temp_max} $UNIT",
      sunrise = unixTime(weatherData.sys.sunrise),
      sunset = unixTime(weatherData.sys.sunset),
      humidity = "${weatherData.main.humidity}$PERCENT",
      wind = "${weatherData.wind.speed} $METERS_PER_SECOND",
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
  // Convert the timestamps to Date objects
    val time = Date(timex * 1000)
  // Format the Date objects to the desired HH:mm format
    val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return dateFormat.format(time)
  }
}