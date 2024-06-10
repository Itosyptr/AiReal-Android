package capstone.tim.aireal.ui.explore

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import capstone.tim.aireal.api.ApiConfig
import capstone.tim.aireal.data.pref.UserPreference
import capstone.tim.aireal.response.DataItem
import capstone.tim.aireal.response.DetailShopResponse
import capstone.tim.aireal.response.ProductsResponse
import capstone.tim.aireal.response.ShopData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExploreViewModel(
    private val pref: UserPreference,
    private val context: Context
) : ViewModel() {

    private val _listProducts = MutableLiveData<List<DataItem?>?>()
    val listProducts: MutableLiveData<List<DataItem?>?> = _listProducts

    var _listData: MutableList<ShopData> = mutableListOf()
    val listData: MutableLiveData<MutableList<ShopData>> = MutableLiveData(_listData)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    companion object {
        private const val TAG = "ExploreViewModel"
    }

    fun getProducts(token: String, category: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getProductbyCategory(token, category)
        client.enqueue(object : Callback<ProductsResponse> {
            override fun onResponse(
                call: Call<ProductsResponse>,
                response: Response<ProductsResponse>
            ) {
                if (response.isSuccessful) {
                    val products = response.body()?.data ?: emptyList()

                    for (product in products) {
                        getDetailShop(token, product?.shopId!!)
                    }

                    Log.d(TAG, "onResponse: ${response.body()?.data}")
                    _listProducts.value = response.body()?.data

                } else {
                    _isError.value = true
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ProductsResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    private fun getDetailShop(token: String, shopId: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getShopDetails(token, shopId)
        client.enqueue(object : Callback<DetailShopResponse> {
            override fun onResponse(
                call: Call<DetailShopResponse>,
                response: Response<DetailShopResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse: ${response.body()?.data}")
                    response.body()?.data?.let { _listData.add(it) }
                } else {
                    _isError.value = true
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailShopResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }


    suspend fun getToken(): String {
        return pref.getToken()
    }
}