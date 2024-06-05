package capstone.tim.aireal.ui.detailProduct

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import capstone.tim.aireal.R
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderAdapter(private val context: Context, private val sliderItems: List<SliderData>) :
    SliderViewAdapter<SliderAdapter.ViewHolder>() {
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val sliderItem = sliderItems[position]
        Glide.with(viewHolder.itemView)
            .load(sliderItem.imgUrl)
            .fitCenter()
            .into(viewHolder.imgPhoto)
    }

    override fun getCount(): Int {
        return sliderItems.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder {
        val view: View =
            LayoutInflater.from(parent!!.context).inflate(R.layout.slider_layout, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(itemView: View) : SliderViewAdapter.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.myimage)
    }
}
