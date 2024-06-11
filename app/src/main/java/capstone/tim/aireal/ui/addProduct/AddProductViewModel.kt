package capstone.tim.aireal.ui.addProduct

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import capstone.tim.aireal.api.ApiConfig
import capstone.tim.aireal.data.pref.UserModel
import capstone.tim.aireal.data.pref.UserPreference
import capstone.tim.aireal.response.CreateProductResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddProductViewModel(
    private val pref: UserPreference,
    private val context: Context
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    fun createProduct(
        token: String,
        shopId: RequestBody,
        categoryId: RequestBody,
        name: RequestBody,
        description: RequestBody,
        longdescription: RequestBody,
        price: RequestBody,
        stock: RequestBody,
        image: List<MultipartBody.Part>
    ) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().addProduct(
            token,
            shopId,
            categoryId,
            name,
            description,
            longdescription,
            price,
            stock,
            image
        )
        client.enqueue(object : Callback<CreateProductResponse> {
            override fun onResponse(
                call: Call<CreateProductResponse>,
                response: Response<CreateProductResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse: ${response.body()?.message}")
                    _isError.value = false
                } else {
                    _isError.value = true
                    Log.e(TAG, "onResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<CreateProductResponse>, t: Throwable) {
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
        private const val TAG = "AddProductViewModel"
    }
}