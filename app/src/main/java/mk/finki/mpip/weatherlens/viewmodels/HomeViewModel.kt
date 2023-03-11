package mk.finki.mpip.weatherlens.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mk.finki.mpip.weatherlens.domain.GetWeatherUseCase
import mk.finki.mpip.weatherlens.network.service.ApiResponse

class HomeViewModel : ViewModel() {

  private val getWeatherUseCase = GetWeatherUseCase()
  private val createWeatherContent = CreateHomeContentData()
  private val _data: MutableLiveData<HomeViewState> = MutableLiveData(HomeViewState.Loading)
  val data: LiveData<HomeViewState>
    get() = _data

  init {
    viewModelScope.launch {
      val response = getWeatherUseCase(
        41.9981,
        21.4254,
        "metric",
        "8d49837c0b8c8f2adc835804b2cdd0fd"
      )

      _data.value = when (response) {
        is ApiResponse.Complete -> createWeatherContent(response.value)
        is ApiResponse.Error -> HomeViewState.Error
        is ApiResponse.Loading -> HomeViewState.Loading
      }
    }
  }
}

sealed interface HomeViewState {
  object Loading : HomeViewState

  object Error : HomeViewState

  data class WeatherContent(
    val weather: String,
    val weatherIcon: Int,
    val minTemperature: String,
    val maxTemperature: String,
    val sunrise: String,
    val sunset: String,
    val humidity: String,
    val wind: String,
    val location: String,
    val city: String
  ) : HomeViewState

}