package mk.finki.mpip.weatherlens.presentation

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import mk.finki.mpip.weatherlens.R
import mk.finki.mpip.weatherlens.databinding.FragmentSunriseBinding
import mk.finki.mpip.weatherlens.viewmodels.sunrise.SunriseViewModel


class SunriseFragment : Fragment(), ImageViewAdapter.ItemClickListener {

  companion object {
    const val REQUEST_IMAGE_CAPTURE = 1

    fun createInstance() = SunriseFragment()
  }

  private var binding: FragmentSunriseBinding? = null
  private lateinit var viewModel: SunriseViewModel
  private lateinit var adapter: ImageViewAdapter

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentSunriseBinding.inflate(layoutInflater)
    return binding?.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setUp()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    binding = null
  }

  private fun setUp() {
    setUpViewModel()
    setUpListeners()

    binding?.galleryLayout?.btnCaptureSunrise?.text = getString(R.string.capture_sunrise)
  }

  private fun setUpListeners() {
    binding?.apply {
      galleryLayout.btnCaptureSunrise.setOnClickListener { setUpCameraPermission() }
      fullscreenImageLayout.closeFullscreenImage.setOnClickListener {
        fullscreenImageLayout.root.isVisible = false
        galleryLayout.root.isVisible = true
      }
    }
  }

  private fun setUpViewModel() {
    viewModel = ViewModelProvider(
      this,
      ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
    )[SunriseViewModel::class.java]

    adapter = ImageViewAdapter()
      .apply { setClickListener(this@SunriseFragment) }
    binding?.galleryLayout?.apply {
      sunriseRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
      sunriseRecyclerView.adapter = adapter
    }

    viewModel.images.observe(requireActivity()) {
      adapter.updateList(it)
    }
  }

  private fun setUpCameraPermission() =
    when {
      permissionIsGranted() -> {
        dispatchTakePictureIntent()
      }
      shouldShowRationale() -> showCameraPermissionDialog()
      else -> requestCameraPermission()
    }

  private fun dispatchTakePictureIntent() {
    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    try {
      startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
    } catch (e: ActivityNotFoundException) {
      // display error state to the user
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
      val extras = data?.extras
      val imageBitmap = extras!!["data"] as Bitmap?
      viewModel.addImage(imageBitmap)
    }
  }

  private fun showCameraPermissionDialog() =
    AlertDialog.Builder(requireContext())
      .setTitle(getString(R.string.camera_permission))
      .setMessage(getString(R.string.camera_permission_rationale_text))
      .setPositiveButton(
        getString(R.string.proceed)
      ) { _, _ ->
        requestCameraPermission()
      }
      .create()
      .show()

  private fun permissionIsGranted() =
    ContextCompat.checkSelfPermission(
      requireContext(),
      Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

  private fun shouldShowRationale() =
    ActivityCompat.shouldShowRequestPermissionRationale(
      requireActivity(),
      Manifest.permission.CAMERA
    )

  private fun requestCameraPermission() =
    requestPermissionLauncher.launch(Manifest.permission.CAMERA)

  private val requestPermissionLauncher =
    registerForActivityResult(
      ActivityResultContracts.RequestPermission()
    ) { isGranted ->
      if (isGranted) {
        dispatchTakePictureIntent()
      } else {
        Toast.makeText(
          requireContext(),
          "Camera permission has been denied",
          Toast.LENGTH_LONG
        ).show()
      }
    }

  override fun onItemClick(view: View?, position: Int) {
    binding?.apply {
      galleryLayout.root.isVisible = false
      fullscreenImageLayout.apply {
        root.isVisible = true
        fullscreenImageView.setImageBitmap(viewModel?.getImage(position))
      }
    }
  }
}