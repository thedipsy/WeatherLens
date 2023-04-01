package mk.finki.mpip.weatherlens.viewmodels.sunrise

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mk.finki.mpip.weatherlens.viewmodels.notifyObserver

class SunriseViewModel : ViewModel() {

  private val _images: MutableLiveData<MutableList<Bitmap>> = MutableLiveData(mutableListOf())
  val images: LiveData<MutableList<Bitmap>>
    get() = _images


  fun addImage(image: Bitmap?) {
    image?.let {
      _images.value?.add(image)
      _images.notifyObserver()
    }
  }

  fun getImage(position: Int): Bitmap? = _images.value?.get(position)


}

