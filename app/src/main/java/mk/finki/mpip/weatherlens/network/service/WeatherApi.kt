package mk.finki.mpip.weatherlens.network.service

import mk.finki.mpip.weatherlens.network.models.WeatherResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

  @GET("2.5/weather")
  suspend fun getWeather(
    @Query("lat") lat: Double,
    @Query("lon") lon: Double,
    @Query("units") units: String?,
    @Query("appid") appid: String?
  ) : Response<WeatherResponse>
}