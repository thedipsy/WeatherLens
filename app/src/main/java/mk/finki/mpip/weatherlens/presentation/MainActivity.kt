package mk.finki.mpip.weatherlens.presentation

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import mk.finki.mpip.weatherlens.databinding.ActivityMainBinding
import mk.finki.mpip.weatherlens.presentation.base.BaseActivity

class MainActivity : BaseActivity() {

  private var binding: ActivityMainBinding? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding?.root)
    setupView()
    setUpLocationPermission()
  }

  private fun setUpLocationPermission() =
    when {
      permissionIsGranted() -> navigateToFragment(fragment = HomeFragment.createInstance())
      shouldShowRationale() -> showLocationPermissionDialog()
      else -> requestLocationPermission()
    }

  private fun permissionIsGranted() =
    ContextCompat.checkSelfPermission(
      this,
      Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

  private fun shouldShowRationale() =
    ActivityCompat.shouldShowRequestPermissionRationale(
      this,
      Manifest.permission.ACCESS_COARSE_LOCATION
    )

  private fun requestLocationPermission() =
    requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)

  private val requestPermissionLauncher =
    registerForActivityResult(
      ActivityResultContracts.RequestPermission()
    ) { isGranted ->
      if (isGranted) {
        navigateToFragment(fragment = HomeFragment.createInstance())
      } else {
        Toast.makeText(this, "Location denied", Toast.LENGTH_LONG).show()
      }
    }

  private fun showLocationPermissionDialog() =
    AlertDialog.Builder(this)
      .setTitle("Location Permission")
      .setMessage("Location is needed to retrieve information about weather forcast.")
      .setPositiveButton(
        "Proceed"
      ) { _, _ ->
        requestLocationPermission()
      }
      .create()
      .show()

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