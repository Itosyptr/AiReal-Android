package capstone.tim.aireal.ui.toko

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProdukViewModel : ViewModel() {
    val listProduk = MutableLiveData<ArrayList<Product>>() // Gunakan model data produk Anda

    fun setListProduk() {
        enqueue()
    }

    data class Product(
        val id: String, // Atau Int, tergantung kebutuhan
        val nama: String,
        val harga: Int,
    )

    fun getListProduk(): LiveData<ArrayList<Product>> {
        return listProduk
    }
}

private fun enqueue() {

}
