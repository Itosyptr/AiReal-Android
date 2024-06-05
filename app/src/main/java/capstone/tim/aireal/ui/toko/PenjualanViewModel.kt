package capstone.tim.aireal.ui.toko

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import capstone.tim.aireal.api.ApiService
import capstone.tim.aireal.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PenjualanViewModel : ViewModel() {

    data class Penjualan(
        val imageResId: Int,
        val name: String,
        val price: String
        // ... tambahkan atribut lain sesuai kebutuhan
    )

    private val _listPenjualan = MutableLiveData<List<Penjualan>>()
    val listPenjualan: LiveData<List<Penjualan>> = _listPenjualan

    init {
        setListPenjualan()
    }

    fun setListPenjualan() {
        RetrofitClient.apiInstance.getPenjualan().enqueue(object : Callback<List<Penjualan>> {
            override fun onResponse(call: Call<List<Penjualan>>, response: Response<List<Penjualan>>) {
                if (response.isSuccessful) {
                    _listPenjualan.value = response.body()
                } else {
                    // Handle error
                }
            }

            override fun onFailure(call: Call<List<Penjualan>>, t: Throwable) {
                // Handle failure
            }
        })
    }
}
