package capstone.tim.aireal.ui.addProduct

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import capstone.tim.aireal.R

class ImageAdapter(private val listImage: ArrayList<Uri>) :
    RecyclerView.Adapter<ImageAdapter.ListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageAdapter.ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_photos, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageAdapter.ListViewHolder, position: Int) {
        val image = listImage[position]
        holder.image.setImageURI(image)
    }

    override fun getItemCount(): Int = listImage.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.myimage)
    }
}