package capstone.tim.aireal.ui.keranjang

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import capstone.tim.aireal.R
import capstone.tim.aireal.databinding.ItemProdukBinding
import capstone.tim.aireal.response.DataItem
import com.bumptech.glide.Glide

class KeranjangAdapter(private val context: Context) :
    ListAdapter<DataItem, KeranjangAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemProdukBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
    }

    class MyViewHolder(private val binding: ItemProdukBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: DataItem) {
            binding.tvProductName.text = product.name
            binding.tvProductPrice.text = context.getString(R.string.product_price, product.price)
            binding.tvQuantity.text = product.stock
            Glide.with(binding.root)
                .load(product.imageUrl?.get(1))
                .into(binding.ivProductImage)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}