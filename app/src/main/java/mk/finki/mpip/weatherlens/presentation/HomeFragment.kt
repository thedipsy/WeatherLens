package mk.finki.mpip.weatherlens.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import mk.finki.mpip.weatherlens.R
import mk.finki.mpip.weatherlens.databinding.FragmentHomeBinding
import mk.finki.mpip.weatherlens.network.NetworkUtil
import mk.finki.mpip.weatherlens.viewmodels.home.HomeViewModel
import mk.finki.mpip.weatherlens.viewmodels.home.HomeViewState

class HomeFragment : Fragment() {

  companion object {
    fun createInstance() = HomeFragment()
  }

  private var binding: FragmentHomeBinding? = null
  private var viewModel: HomeViewModel? = null

  private var fusedLocationClient: FusedLocationProviderClient? = null

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
    getLocation()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    binding = null
  }

  private fun setUp() {
    setUpViewModel()
    setUpListeners()

    fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
  }

  private fun setUpViewModel() {
    viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
    viewModel?.data?.observe(viewLifecycleOwner) { data ->
      updateUi(data)
    }

    viewModel?.location?.observe(viewLifecycleOwner) { _ ->
      getWeatherData()
    }
  }

  private fun setUpListeners() {
    binding?.apply {
      pullToRefresh.setOnRefreshListener {
        getLocation()
        pullToRefresh.isRefreshing = false
      }
      noNetworkLayout.btnRetry.setOnClickListener {
        getLocation()
      }
      noLocationLayout.btnRetry.setOnClickListener {
        getLocation()
      }
      errorLayout.btnRetry.setOnClickListener {
        getLocation()
      }
    }
  }

  private fun getLocation() {
    fusedLocationClient
      ?.getCurrentLocation(PRIORITY_HIGH_ACCURACY, null)
      ?.addOnSuccessListener { location ->
        viewModel?.setLocation(location?.latitude, location?.longitude)
      }
  }

  private fun getWeatherData() {
    if (NetworkUtil.isOnline(requireContext())) {
      viewModel?.getWeatherData()
    } else {
      showNoConnectivityScreen()
    }
  }

  private fun updateUi(data: HomeViewState) {
    when (data) {
      is HomeViewState.Error -> showErrorScreen()
      is HomeViewState.Loading -> showLoadingScreen()
      is HomeViewState.WeatherContent -> showContentScreen(data)
      is HomeViewState.NoLocationEnabled -> showNoLocationScreen()
    }
  }

  private fun showContentScreen(weatherData: HomeViewState.WeatherContent) {
    binding?.apply {
      contentLayout.root.isVisible = true
      loadingLayout.isVisible = false
      errorLayout.root.isVisible = false
      noNetworkLayout.root.isVisible = false
      noLocationLayout.root.isVisible = false

      contentLayout.tvWeather.text = weatherData.weather
      contentLayout.ivWeather.setImageResource(weatherData.weatherIcon)
      contentLayout.tvTemperatureMin.text = weatherData.minTemperature
      contentLayout.tvTemperatureMax.text = weatherData.maxTemperature
      contentLayout.tvHumidity.text = weatherData.humidity
      contentLayout.tvWind.text = weatherData.wind
      contentLayout.tvLocation.text = weatherData.location
      contentLayout.tvLocationCity.text = weatherData.city
      contentLayout.tvSunrise.text = weatherData.sunrise
      contentLayout.tvSunset.text = weatherData.sunset
    }
  }

  private fun showLoadingScreen() {
    binding?.apply {
      loadingLayout.isVisible = true
      contentLayout.root.isVisible = false
      errorLayout.root.isVisible = false
      noNetworkLayout.root.isVisible = false
      noLocationLayout.root.isVisible = false
    }
  }

  private fun showErrorScreen() {
    binding?.apply {
      errorLayout.root.isVisible = true
      loadingLayout.isVisible = false
      contentLayout.root.isVisible = false
      noNetworkLayout.root.isVisible = false
      noLocationLayout.root.isVisible = false
    }
  }

  private fun showNoLocationScreen() {
    binding?.apply {
      errorLayout.root.isVisible = false
      loadingLayout.isVisible = false
      contentLayout.root.isVisible = false
      noNetworkLayout.root.isVisible = false
      noLocationLayout.root.isVisible = true
    }
  }

  private fun showNoConnectivityScreen() {
    binding?.apply {
      noNetworkLayout.root.isVisible = true
      errorLayout.root.isVisible = false
      loadingLayout.isVisible = false
      contentLayout.root.isVisible = false
      noLocationLayout.root.isVisible = false
    }
  }
}