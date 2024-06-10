package capstone.tim.aireal.ui.orderHistory

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import capstone.tim.aireal.api.ApiConfig
import capstone.tim.aireal.data.pref.UserModel
import capstone.tim.aireal.data.pref.UserPreference
import capstone.tim.aireal.response.DataItem
import capstone.tim.aireal.response.DetailItem
import capstone.tim.aireal.response.ProductByIdResponse
import capstone.tim.aireal.response.UserOrderResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderHistoryViewModel(
    private val pref: UserPreference,
    private val context: Context
) : ViewModel() {

    private val _listOrder = MutableLiveData<List<DetailItem?>?>()
    val listOrder: MutableLiveData<List<DetailItem?>?> = _listOrder

    private var listProducts: MutableList<DataItem> = mutableListOf()
    var mutableLiveDataProducts: MutableLiveData<List<DataItem>> = MutableLiveData(listProducts)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    fun getOrderHistory(token: String, userId: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserOrder(token, userId)
        client.enqueue(object : Callback<UserOrderResponse> {
            override fun onResponse(
                call: Call<UserOrderResponse>,
                response: Response<UserOrderResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse: ${response.body()?.data}")
                    _listOrder.value = response.body()?.data?.get(0)?.items

                    val orders = response.body()?.data?.get(0)?.items ?: emptyList()

                    for (order in orders) {
                        getDetailProduct(token, order?.productId!!)
                    }

                } else {
                    _isError.value = true
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserOrderResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getDetailProduct(token: String, id: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getProductDetails(token, id)
        client.enqueue(object : Callback<ProductByIdResponse> {
            override fun onResponse(
                call: Call<ProductByIdResponse>,
                response: Response<ProductByIdResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse: ${response.body()?.data}")
                    listProducts.add(response.body()?.data as DataItem)
                } else {
                    _isError.value = true
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ProductByIdResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    companion object {
        private const val TAG = "OrderHistoryViewModel"
    }
}