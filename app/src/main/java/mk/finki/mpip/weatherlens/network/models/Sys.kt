package mk.finki.mpip.weatherlens.network.models

import java.io.Serializable

data class Sys(
  val message: Double,
  val country: String,
  val sunrise: Long,
  val sunset: Long,
  val type: Int
) : Serializable