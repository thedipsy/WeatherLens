package mk.finki.mpip.weatherlens.repository

import androidx.lifecycle.LiveData

class WeatherRepository(private val weatherDao: WeatherDao) {

  suspend fun  getAllImages() : List<ImageCapture> = weatherDao.getAllImages()

  suspend fun insertImage(image: ImageCapture) = weatherDao.insertImage(image)

  suspend fun deleteImage(image: ImageCapture) = weatherDao.deleteImage(image)

}