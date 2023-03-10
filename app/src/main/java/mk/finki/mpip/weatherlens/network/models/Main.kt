package mk.finki.mpip.weatherlens.network.models

import java.io.Serializable

data class Main(
  val temp: Double,
  val pressure: Double,
  val humidity: Int,
  val temp_min: Double,
  val temp_max: Double,
  val sea_lever: Double,
  val grnd_level: Double
) : Serializable