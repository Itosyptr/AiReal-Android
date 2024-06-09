package capstone.tim.aireal.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import capstone.tim.aireal.databinding.ItemProductCardBinding
import capstone.tim.aireal.response.DataItem
import capstone.tim.aireal.ui.detailProduct.DetailProductActivity
import com.bumptech.glide.Glide

class ProductsAdapter : ListAdapter<DataItem, ProductsAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemProductCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)

        val data = product?.let { it1 ->
            DataItem(
                it1.createdAt,
                it1.price,
                it1.imageUrl,
                it1.longdescription,
                it1.name,
                it1.description,
                it1.id,
                it1.shopId,
                it1.stock,
                it1.categoryId,
                it1.updatedAt
            )
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, DetailProductActivity::class.java)
            intent.putExtra(DetailProductActivity.LIST_PRODUCT, data)
            it.context.startActivity(intent)
        }
    }

    class MyViewHolder(private val binding: ItemProductCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: DataItem) {
            binding.productName.text = product.name
            binding.productLocation.text = product.longdescription
            binding.productPrice.text = product.price
            Glide.with(binding.root)
                .load(product.imageUrl?.get(1))
                .into(binding.productImage)
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