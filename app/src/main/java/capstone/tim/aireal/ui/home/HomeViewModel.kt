package capstone.tim.aireal.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import capstone.tim.aireal.api.ApiConfig
import capstone.tim.aireal.data.pref.UserPreference
import capstone.tim.aireal.response.DataItem
import kotlinx.coroutines.launch
import retrofit2.await

class HomeViewModel(
    private val pref: UserPreference,
    private val context: Context
) : ViewModel() {

    private val _listProducts = MutableLiveData<List<DataItem?>?>()
    val listProducts: MutableLiveData<List<DataItem?>?> = _listProducts

    var _listData: MutableList<String> = mutableListOf()
    val listData: MutableLiveData<MutableList<String>> = MutableLiveData(_listData)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    companion object {
        private const val TAG = "MainViewModel"
    }

    fun getProducts(token: String) {
        _isLoading.value = true

        viewModelScope.launch {
            val productResponse = try {
                ApiConfig.getApiService().getProduct(token).await()
            } catch (e: Exception) {
                _isError.value = true
                Log.e(TAG, "onFailure (getProducts): ${e.message}")
                return@launch
            }

            if (productResponse.status == "success") {
                val products = productResponse.data ?: emptyList()

                val productDetails = mutableListOf<String>()
                for (product in products) {
                    val detailResponse =
                        ApiConfig.getApiService().getShopDetails(token, product?.shopId!!).await()

                    if (detailResponse.status == "success") {
                        productDetails.add(detailResponse.data?.city.toString())
                    } else {
                        Log.e(TAG, "onFailure (getProductDetails): ${detailResponse.data}")
                    }
                }

                _listProducts.postValue(products)
                _listData.addAll(productDetails)
            } else {
                _isError.value = true
                Log.e(TAG, "onFailure: ${productResponse.data}")
            }

            _isLoading.value = false
        }
    }

    suspend fun getToken(): String {
        return pref.getToken()
    }
}