package capstone.tim.aireal.ui.orderHistory

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
import capstone.tim.aireal.response.DataItem
import capstone.tim.aireal.response.ProductOrderItem
import kotlinx.coroutines.launch
import retrofit2.await

class OrderHistoryViewModel(
    private val pref: UserPreference,
    private val context: Context
) : ViewModel() {

    private val _listCart = MutableLiveData<List<ProductOrderItem?>?>()
    val listProducts: MutableLiveData<List<ProductOrderItem?>?> = _listCart

    var _listData: MutableList<DataItem> = mutableListOf()
    val listData: MutableLiveData<MutableList<DataItem>> = MutableLiveData(_listData)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    fun getOrderByUserId(token: String, userId: String) {
        _isLoading.value = true

        viewModelScope.launch {
            val orderResponse = try {
                ApiConfig.getApiService().getUserOrder(token, userId).await()
            } catch (e: Exception) {
                _isError.value = true
                Log.e(TAG, "onFailure (getCart): ${e.message}")
                return@launch
            }

            if (orderResponse.status == "success") {
                val order = orderResponse.data ?: emptyList()

                val productDetails = mutableListOf<DataItem>()
                val productData = mutableListOf<ProductOrderItem>()
                for (orderItem in order) {
                    val productOrder = orderItem?.items ?: emptyList()
                    for (product in productOrder) {
                        val detailResponse =
                            ApiConfig.getApiService().getProductDetails(token, product?.productId!!)
                                .await()

                        if (detailResponse.status == "success") {
                            productDetails.add(
                                DataItem(
                                    imageUrl = detailResponse.data!!.imageUrl,
                                    name = detailResponse.data.name,
                                    price = detailResponse.data.price
                                )
                            )
                            productData.add(
                                ProductOrderItem(
                                    quantity = product.quantity,
                                    productId = product.productId
                                )
                            )
                        } else {
                            Log.e(TAG, "onFailure (getProductDetails): ${detailResponse.data}")
                        }
                    }
                }

                _listCart.postValue(productData)
                _listData.addAll(productDetails)
            } else {
                _isError.value = true
                Log.e(TAG, "onFailure (getCart): ${orderResponse.data}")
            }

            _isLoading.postValue(false)
        }
    }

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    companion object {
        private const val TAG = "OrderHistoryViewModel"
    }
}