package mk.finki.mpip.weatherlens.presentation

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import mk.finki.mpip.weatherlens.R
import mk.finki.mpip.weatherlens.databinding.ActivityMainBinding
import mk.finki.mpip.weatherlens.presentation.base.BaseActivity

class MainActivity : BaseActivity() {

  companion object {
    private const val REQUEST_CHECK_SETTINGS = 123
  }

  private var binding: ActivityMainBinding? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding?.root)
    setupView()

    setUpLocationPermission()
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == REQUEST_CHECK_SETTINGS) {
        navigateToHome()
    }
  }

  private fun checkForChangeLocationSettings() {
    val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).apply {
      setMinUpdateDistanceMeters(500F)
      setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
      setWaitForAccurateLocation(true)
    }.build()

    val locationSettingsRequest = LocationSettingsRequest.Builder()
      .addLocationRequest(locationRequest)
      .build()

    val client = LocationServices.getSettingsClient(this)
    val task = client.checkLocationSettings(locationSettingsRequest)

    task.addOnSuccessListener {
        navigateToHome()
    }
      .addOnFailureListener { exception ->
        if (exception is ResolvableApiException) {
          try {
            exception.startResolutionForResult(
              this@MainActivity,
              REQUEST_CHECK_SETTINGS
            )
          } catch (sendEx: IntentSender.SendIntentException) {
            // Ignore the error.
          }
        }
      }
  }

  private fun navigateToHome() = navigateToFragment(fragment = HomeFragment.createInstance())

  private fun setUpLocationPermission() =
    when {
      permissionIsGranted() -> {
        checkForChangeLocationSettings()
      }
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
        setLocationPermissionDeniedScreenVisibility(false)
        checkForChangeLocationSettings()
      } else {
        setLocationPermissionDeniedScreenVisibility(true)
      }
    }

  private fun setLocationPermissionDeniedScreenVisibility(visible: Boolean) {
    binding?.apply {
      deniedPermissionLayout.root.isVisible = visible
      bottomNavigation.root.isVisible = !visible
    }
  }

  private fun showLocationPermissionDialog() =
    AlertDialog.Builder(this)
      .setTitle(getString(R.string.location_permission))
      .setMessage(getString(R.string.location_permission_rationale_text))
      .setPositiveButton(
        getString(R.string.proceed)
      ) { _, _ ->
        requestLocationPermission()
      }
      .create()
      .show()

  private fun setupView() {
    binding?.apply {
      deniedPermissionLayout.btnRetry.setOnClickListener { setUpLocationPermission() }

      bottomNavigation.apply {
        buttonHome.setOnClickListener {
          navigateToFragment(fragment = HomeFragment.createInstance())
        }
        buttonSunrise.setOnClickListener {
          navigateToFragment(fragment = SunriseFragment.createInstance())
        }
        buttonSunset.setOnClickListener {
          navigateToFragment(fragment = SunsetFragment.createInstance())
        }
      }
    }
  }
}