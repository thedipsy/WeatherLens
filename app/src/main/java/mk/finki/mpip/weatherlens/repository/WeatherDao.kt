package mk.finki.mpip.weatherlens.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WeatherDao {

  @Insert
  suspend fun insertImage(imageCapture: ImageCapture) : Long

  @Delete
  suspend fun deleteImage(imageCapture: ImageCapture)

  @Query("SELECT * FROM imageCapture ORDER BY timestamp DESC")
  fun getAllImages(): List<ImageCapture>

}