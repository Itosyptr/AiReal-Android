package capstone.tim.aireal.ui.toko

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import capstone.tim.aireal.R
import capstone.tim.aireal.databinding.ItemProdukBinding

class TokoAdapter : RecyclerView.Adapter<TokoAdapter.TokoViewHolder>() {

    private val tokoList = ArrayList<Toko>()

    fun setList(toko: List<Toko>) {
        tokoList.clear()
        tokoList.addAll(toko)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TokoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_produk, parent, false)
        return TokoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TokoViewHolder, position: Int) {
        holder.bind(tokoList[position])
    }

    override fun getItemCount(): Int = tokoList.size

    class TokoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemProdukBinding.bind(itemView)

        fun bind(toko: toko) {
            binding.apply {
                ivProductImage.setImageResource(toko.imageResId) // Assuming image is a drawable resource ID
                tvProductName.text = toko.name
                tvProductPrice.text = toko.price.toString()
            }
        }
    }
}
