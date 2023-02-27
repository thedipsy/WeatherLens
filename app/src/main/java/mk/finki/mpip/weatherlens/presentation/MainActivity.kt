package mk.finki.mpip.weatherlens.presentation

import android.os.Bundle
import mk.finki.mpip.weatherlens.databinding.ActivityMainBinding
import mk.finki.mpip.weatherlens.presentation.base.BaseActivity

class MainActivity : BaseActivity() {

  private var binding : ActivityMainBinding? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding?.root)

    setupView()

    navigateToFragment(fragment = HomeFragment.createInstance())
  }

  private fun setupView() {
    binding?.bottomNavigation?.buttonHome?.setOnClickListener {
      navigateToFragment(fragment = HomeFragment.createInstance())
    }
    binding?.bottomNavigation?.buttonSunrise?.setOnClickListener {
      navigateToFragment(fragment = SunriseFragment.createInstance())
    }
    binding?.bottomNavigation?.buttonSunset?.setOnClickListener {
      navigateToFragment(fragment = SunsetFragment.createInstance())
    }
  }
}