package mk.finki.mpip.weatherlens.viewmodels.sunset.domain

import mk.finki.mpip.weatherlens.repository.ImageCapture
import mk.finki.mpip.weatherlens.repository.WeatherDao
import mk.finki.mpip.weatherlens.repository.WeatherRepository

class InsertImageCapture(dao: WeatherDao) {

  private val repository: WeatherRepository = WeatherRepository(dao)

  suspend operator fun invoke(imageCapture: ImageCapture) =
    repository.insertImage(imageCapture)

}