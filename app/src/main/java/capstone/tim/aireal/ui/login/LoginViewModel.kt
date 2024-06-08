package capstone.tim.aireal.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import capstone.tim.aireal.api.ApiConfig
import capstone.tim.aireal.data.pref.UserModel
import capstone.tim.aireal.data.pref.UserPreference
import capstone.tim.aireal.response.DataLogin
import capstone.tim.aireal.response.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: UserPreference) : ViewModel() {
    val data = MutableLiveData<LoginResponse>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun login(dataLogin: DataLogin) {
        _isLoading.value = true
        val loginRequest = ApiConfig.getApiService().login(dataLogin)
        loginRequest?.enqueue(object : Callback<LoginResponse?> {
            override fun onResponse(
                call: Call<LoginResponse?>,
                response: Response<LoginResponse?>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    data.value = response.body()
                } else {
                    _isError.value = true
                    _errorMessage.value = response.message()
                }
            }

            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
            }
        })
    }


    fun saveUser(user: UserModel) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }

    fun getLogin(): LiveData<LoginResponse> {
        return data
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}