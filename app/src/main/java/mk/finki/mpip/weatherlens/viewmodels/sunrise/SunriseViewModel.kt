package mk.finki.mpip.weatherlens.viewmodels.sunrise

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mk.finki.mpip.weatherlens.repository.ImageCapture
import mk.finki.mpip.weatherlens.repository.ImageType
import mk.finki.mpip.weatherlens.repository.WeatherDao
import mk.finki.mpip.weatherlens.repository.WeatherDatabase
import mk.finki.mpip.weatherlens.viewmodels.sunset.domain.DeleteImageCapture
import mk.finki.mpip.weatherlens.viewmodels.sunset.domain.GetAllImageCapture
import mk.finki.mpip.weatherlens.viewmodels.sunset.domain.InsertImageCapture
import java.time.LocalDateTime

class SunriseViewModel(application: Application) : AndroidViewModel(application) {

  //use case
  private val dao: WeatherDao = WeatherDatabase.getDatabase(application).getImagesDao()
  private val insertImageCapture = InsertImageCapture(dao)
  private val deleteImageCapture = DeleteImageCapture(dao)
  private val getAllImageCaptures = GetAllImageCapture(dao)

  private val _images: MutableLiveData<List<ImageCapture>> = MutableLiveData(mutableListOf())
  val images: LiveData<List<ImageCapture>>
    get() = _images

  init {
    getAllImages()
  }

  fun addImage(image: Bitmap?) {
    image ?: return
    viewModelScope.launch(Dispatchers.IO) {
      val imageCapture = ImageCapture(
        imageBitmap = image,
        timestamp = LocalDateTime.now(),
        type = ImageType.SUNRISE
      )
      val outcome = insertImageCapture.invoke(imageCapture)
      if (outcome > 0L) {
        getAllImages()
      }
    }
  }

  fun deleteImage(imageCapture: ImageCapture) {
    viewModelScope.launch(Dispatchers.IO) {
      deleteImageCapture(imageCapture)
    }
    getAllImages()
  }

  private fun getAllImages() {
    viewModelScope.launch(Dispatchers.IO) {
      val allImages = getAllImageCaptures()
      val sunriseImages = allImages.filter { i -> i.type == ImageType.SUNRISE }
      _images.postValue(sunriseImages)
    }
  }

  fun getImage(position: Int) = _images.value?.get(position)?.imageBitmap

}