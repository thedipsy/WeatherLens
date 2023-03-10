package mk.finki.mpip.weatherlens.domain

import mk.finki.mpip.weatherlens.network.models.WeatherResponse
import mk.finki.mpip.weatherlens.network.service.ApiResponse
import mk.finki.mpip.weatherlens.network.service.RetrofitService
import mk.finki.mpip.weatherlens.network.service.WeatherApi

class GetWeatherUseCase {

  private val api: WeatherApi = RetrofitService.api()

  suspend operator fun invoke(
    lat: Double,
    lon: Double,
    units: String?,
    appid: String?
  ): ApiResponse<WeatherResponse> {
    val response = api.getWeather(lat, lon, units, appid)

    return if (response.isSuccessful) {
      response.body()?.let { weatherResponse ->
        ApiResponse.Complete(weatherResponse)
      } ?: ApiResponse.Error
    } else {
      ApiResponse.Error
    }
  }
}