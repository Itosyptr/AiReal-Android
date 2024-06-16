package capstone.tim.aireal.ui.keranjang

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import capstone.tim.aireal.api.ApiConfig
import capstone.tim.aireal.data.pref.UserModel
import capstone.tim.aireal.data.pref.UserPreference
import capstone.tim.aireal.response.CartItem
import capstone.tim.aireal.response.DataItem
import capstone.tim.aireal.response.OrderResponse
import capstone.tim.aireal.response.dataOrder
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await

class KeranjangViewModel(
    private val pref: UserPreference,
    private val context: Context
) : ViewModel() {

    private val _listCart = MutableLiveData<List<CartItem?>?>()
    val listProducts: MutableLiveData<List<CartItem?>?> = _listCart

    var _listData: MutableList<DataItem> = mutableListOf()
    val listData: MutableLiveData<MutableList<DataItem>> = MutableLiveData(_listData)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    companion object {
        private const val TAG = "CartViewModel"
    }

    fun getCart(token: String, userId: String) {
        _isLoading.value = true

        viewModelScope.launch {
            val cartResponse = try {
                ApiConfig.getApiService().getCart(token, userId).await()
            } catch (e: Exception) {
                _isError.value = true
                Log.e(TAG, "onFailure (getCart): ${e.message}")
                return@launch
            }

            if (cartResponse.status == "success") {
                val cart = cartResponse.data?.items ?: emptyList()

                val productDetails = mutableListOf<DataItem>()
                for (cartItem in cart) {
                    val detailResponse =
                        ApiConfig.getApiService().getProductDetails(token, cartItem?.productId!!)
                            .await()

                    if (detailResponse.status == "success") {
                        productDetails.add(
                            DataItem(
                                imageUrl = detailResponse.data!!.imageUrl,
                                name = detailResponse.data!!.name,
                                price = detailResponse.data!!.price
                            )
                        )
                    } else {
                        Log.e(TAG, "onFailure (getProductDetails): ${detailResponse.data}")
                    }
                }

                _listCart.postValue(cart)
                _listData.addAll(productDetails)
            } else {
                _isLoading.value = false
                _isError.value = true
                Log.e(TAG, "onFailure (getCart): ${cartResponse.data}")
            }

            _isLoading.postValue(false)
        }
    }

    fun orderCart(token: String, dataOrder: dataOrder) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().createOrder(token, dataOrder)
        client.enqueue(object : Callback<OrderResponse> {
            override fun onResponse(
                call: Call<OrderResponse>,
                response: Response<OrderResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse: ${response.body()}")
                } else {
                    _isError.value = true
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(p0: Call<OrderResponse>, p1: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.e(TAG, "onFailure: ${p1.message.toString()}")
            }
        })
    }

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }
}