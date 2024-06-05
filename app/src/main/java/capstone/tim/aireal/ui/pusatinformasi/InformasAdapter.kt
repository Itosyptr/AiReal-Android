package capstone.tim.aireal.ui.pusatinformasi

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import capstone.tim.aireal.databinding.ItemPusatInformasiBinding

class InformasiAdapter(private val listInformasi: List<InformasiItem>) :
    RecyclerView.Adapter<InformasiAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemPusatInformasiBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPusatInformasiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val informasi = listInformasi[position]
        holder.binding.tvJudulInformasi.text = informasi.judul
        holder.binding.tvIsiInformasi.text = informasi.isi
    }

    override fun getItemCount(): Int = listInformasi.size
}

data class InformasiItem(
    val judul: String,
    val isi: String
)