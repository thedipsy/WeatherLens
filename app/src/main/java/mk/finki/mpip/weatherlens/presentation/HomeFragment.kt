package mk.finki.mpip.weatherlens.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.LocationServices
import mk.finki.mpip.weatherlens.databinding.FragmentHomeBinding
import mk.finki.mpip.weatherlens.viewmodels.home.HomeViewModel
import mk.finki.mpip.weatherlens.viewmodels.home.HomeViewState

class HomeFragment : Fragment() {

  companion object {
    fun createInstance() = HomeFragment()
  }

  private var binding: FragmentHomeBinding? = null
  private var viewModel: HomeViewModel? = null

  private var lat: Double? = null
  private var lon: Double? = null

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentHomeBinding.inflate(layoutInflater)
    return binding?.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setUp()

  }

  private fun setUp() {
    viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

    viewModel?.data?.observe(viewLifecycleOwner) { data ->
      updateUi(data)
    }

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    fusedLocationClient.lastLocation
      .addOnSuccessListener { location ->
        lat = location.latitude
        lon = location.longitude
        getWeatherData()
      }

    binding?.apply {
      pullToRefresh.setOnRefreshListener {
        getWeatherData()
        pullToRefresh.isRefreshing = false;
      }
    }
  }

  private fun getWeatherData() {
    viewModel?.getWeatherData(lat, lon)
  }

  private fun updateUi(data: HomeViewState) {
    when (data) {
      is HomeViewState.Error -> showErrorScreen()
      is HomeViewState.Loading -> showLoadingScreen()
      is HomeViewState.WeatherContent -> showContentScreen(data)
    }
  }

  private fun showContentScreen(weatherData: HomeViewState.WeatherContent) {
    binding?.apply {
      contentLayout.root.isVisible = true
      loadingLayout.isVisible = false
      errorLayout.isVisible = false

      contentLayout.tvWeather.text = weatherData.weather
      contentLayout.ivWeather.setImageResource(weatherData.weatherIcon)
      contentLayout.tvTemperatureMin.text = weatherData.minTemperature
      contentLayout.tvTemperatureMax.text = weatherData.maxTemperature
      contentLayout.tvHumidity.text = weatherData.humidity
      contentLayout.tvWind.text = weatherData.wind
      contentLayout.tvLocation.text = weatherData.location
      contentLayout.tvLocationCity.text = weatherData.city
    }
  }

  private fun showLoadingScreen() {
    binding?.apply {
      loadingLayout.isVisible = true
      contentLayout.root.isVisible = false
      errorLayout.isVisible = false
    }
  }

  private fun showErrorScreen() {
    binding?.apply {
      errorLayout.isVisible = true
      loadingLayout.isVisible = false
      contentLayout.root.isVisible = false
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    binding = null
  }
}