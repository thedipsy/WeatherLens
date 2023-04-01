package mk.finki.mpip.weatherlens.presentation

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import mk.finki.mpip.weatherlens.R
import mk.finki.mpip.weatherlens.repository.ImageCapture


class ImageViewAdapter(private val images: List<ImageCapture>? = null) :
  RecyclerView.Adapter<ImageViewAdapter.ViewHolder>() {

  private var mClickListener: ItemClickListener? = null

  private val allImages = ArrayList<ImageCapture>()
  override fun getItemCount() = allImages.size

  fun updateList(newList: List<ImageCapture>) {
    allImages.clear()
    allImages.addAll(newList)
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewAdapter.ViewHolder {
    val context = parent.context
    val inflater = LayoutInflater.from(context)
    val view = inflater.inflate(R.layout.item_image, parent, false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(viewHolder: ImageViewAdapter.ViewHolder, position: Int) {
    val imageBitmap: Bitmap = allImages[position].imageBitmap
    viewHolder.imageView.setImageBitmap(imageBitmap)
  }

  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    val imageView: ImageView = itemView.findViewById(R.id.image_view)

    init {
      imageView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
      mClickListener?.onItemClick(imageView, adapterPosition)
    }
  }

  fun getItem(id: Int) = allImages[id]

  fun setClickListener(itemClickListener: ItemClickListener) {
    this.mClickListener = itemClickListener
  }

  interface ItemClickListener {
    fun onItemClick(view: View?, position: Int)
  }
}