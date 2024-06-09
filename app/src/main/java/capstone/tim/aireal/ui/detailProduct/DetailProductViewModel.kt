package capstone.tim.aireal.ui.detailProduct

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import capstone.tim.aireal.api.ApiConfig
import capstone.tim.aireal.data.pref.UserModel
import capstone.tim.aireal.data.pref.UserPreference
import capstone.tim.aireal.response.CartRequest
import capstone.tim.aireal.response.CartResponse
import capstone.tim.aireal.response.DataItem
import capstone.tim.aireal.response.DetailShopResponse
import capstone.tim.aireal.response.ShopData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailProductViewModel(
    private val pref: UserPreference,
    private val context: Context
): ViewModel() {
    private val _shopDetail = MutableLiveData<ShopData?>()
    val shopDetail: MutableLiveData<ShopData?> = _shopDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    fun getDetailShop(token: String, shopId: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getShopDetails(token, shopId)
        client.enqueue(object : Callback<DetailShopResponse> {
            override fun onResponse(
                call: Call<DetailShopResponse>,
                response: Response<DetailShopResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse: ${response.body()?.data}")
                    _shopDetail.value = response.body()?.data
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

    fun addToCart(token: String, dataCart: CartRequest) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().addToCart(token, dataCart)
        client.enqueue(object : Callback<CartResponse> {
            override fun onResponse(
                call: Call<CartResponse>,
                response: Response<CartResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse: ${response.body()?.data}")
                } else {
                    _isError.value = true
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(p0: Call<CartResponse>, p1: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.e(TAG, "onFailure: ${p1.message.toString()}")
            }
        })
    }

    suspend fun getToken(): String {
        return pref.getToken()
    }

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    companion object {
        private const val TAG = "DetailProductViewModel"
    }
}