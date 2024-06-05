package capstone.tim.aireal.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import capstone.tim.aireal.R

class ProductCategoriesAdapter(private val listCategories: ArrayList<Categories>) :
    RecyclerView.Adapter<ProductCategoriesAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (photo, name) = listCategories[position]
        holder.imgPhoto.setImageResource(photo)
        holder.tvName.text = name
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listCategories[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int = listCategories.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.image_category)
        val tvName: TextView = itemView.findViewById(R.id.text_category)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Categories)
    }
}