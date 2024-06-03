package capstone.tim.aireal.ui.toko

import android.util.Log
import capstone.tim.aireal.R
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PenjualanViewModel: ViewModel() {
    val listPenjualan = MutableLiveData<ArrayList<Toko>>()

    fun setListPenjualan(username:String){
        Retrofitclient.apiInstance
            .getFollowing(username)
            .enqueue(object : Callback<ArrayList<Toko>> {
                override fun onResponse(
                    call: Call<ArrayList<Toku>>,
                    response: Response<ArrayList<Toko>>
                ) {
                    if (response.isSuccessful) {
                        listPenjualan.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<ArrayList<Toko>>, t: Throwable) {
                    t.message?.let { Log.d("Failure", it) }
                }

            })
    }
    fun getListFollowing(): LiveData<ArrayList<Toko>>{
        return listPenjualan
    }
}