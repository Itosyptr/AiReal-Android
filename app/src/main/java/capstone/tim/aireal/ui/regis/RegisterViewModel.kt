package capstone.tim.aireal.ui.regis

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import capstone.tim.aireal.api.ApiConfig
import capstone.tim.aireal.response.DataRegister
import capstone.tim.aireal.response.regisbg.RegisterResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    private val _registerResult = MutableLiveData<RegisterResponse?>()
    val registerResult: LiveData<RegisterResponse?> get() = _registerResult
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val registerData = DataRegister(name = name, email = email, password = password)
            try {
                val response: Response<RegisterResponse> = ApiConfig.getApiService().register(registerData)
                if (response.isSuccessful) { // Use isSuccessful from Response<T>
                    _registerResult.value = response.body()
                } else {
                    _errorMessage.value = response.body()?.message ?: "Registration failed. Please try again."
                }
            } catch (e: HttpException) {
                _errorMessage.value = "Network error. Please check your internet connection."
            } catch (e: Exception) {
                _errorMessage.value = "An unexpected error occurred. Please try again later."
                Log.e("RegisterViewModel", "Error during registration:", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
