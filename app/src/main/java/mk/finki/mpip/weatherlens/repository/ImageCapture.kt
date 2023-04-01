package mk.finki.mpip.weatherlens.repository

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "imageCapture")
data class ImageCapture(
  @PrimaryKey(autoGenerate = true)
  var id: Int = 0,

  val imageBitmap: Bitmap,

  val type: ImageType,

  val timestamp: LocalDateTime
)

enum class ImageType { SUNSET, SUNRISE }