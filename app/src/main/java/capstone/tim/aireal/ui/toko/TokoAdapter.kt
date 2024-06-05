package capstone.tim.aireal.ui.toko

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import capstone.tim.aireal.databinding.ItemTokoBinding

class TokoAdapter(
    private var products: List<PenjualanViewModel.Penjualan>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<TokoAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(product: PenjualanViewModel.Penjualan)
        fun onItemLongClick(product: PenjualanViewModel.Penjualan)
    }

    inner class ViewHolder(private val binding: ItemTokoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: PenjualanViewModel.Penjualan) {
            binding.ivProductImage.setImageResource(product.imageResId)
            binding.tvProductName.text = product.name
            binding.tvProductPrice.text = "Rp. ${product.price}"

            binding.root.setOnClickListener {
                onItemClickListener.onItemClick(product)
            }
            binding.root.setOnLongClickListener {
                onItemClickListener.onItemLongClick(product)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTokoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size

    fun updateProducts(newProducts: List<PenjualanViewModel.Penjualan>) {
        this.products = newProducts
        notifyDataSetChanged()
    }
}
