package capstone.tim.aireal.ui.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import capstone.tim.aireal.MainActivity
import capstone.tim.aireal.api.ApiConfig
import capstone.tim.aireal.data.pref.UserModel
import capstone.tim.aireal.data.pref.UserPreference
import capstone.tim.aireal.response.DataLogin
import capstone.tim.aireal.response.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: UserPreference, @SuppressLint("StaticFieldLeak") private val context: Context) : ViewModel() {


    private val _loginResult = MutableLiveData<LoginResponse?>()
    val loginResult: MutableLiveData<LoginResponse?> = _loginResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun login(dataLogin: DataLogin) {
        _isLoading.value = true // Tampilkan loading indicator

        ApiConfig.getApiService().login(dataLogin).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false // Sembunyikan loading indicator

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    _loginResult.value = loginResponse

                    loginResponse?.let {
                        if (!loginResponse.error) {
                            // Login berhasil, simpan data dan token ke DataStore
                            viewModelScope.launch {
                                loginResponse.data?.let { userData ->
                                    pref.saveUser(UserModel(
                                        name = userData.name ?: "",
                                        email = userData.email ?: "",
                                        userId = userData.id ?: "",
                                        token = loginResponse.token ?: "",
                                        isLogin = true
                                    ))
                                }
                                startMainActivity()
                            }
                        } else {
                            _isError.value = true
                            _errorMessage.value = response.message() ?: "Login gagal. Silakan coba lagi."
                            Log.e(TAG, "onFailure: ${response.message()}")
                        }
                    }
                } else {
                    _isError.value = true
                    _errorMessage.value = response.message() ?: "Login gagal. Silakan coba lagi."
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                _errorMessage.value = t.message.toString()
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    private fun startMainActivity() {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
    fun saveUser(userModel: UserModel) {
        viewModelScope.launch {
            try {
                pref.saveUser(userModel)
                Log.d(TAG, "User saved successfully") // Opsional: logging untuk debugging
            } catch (e: Exception) {
                _isError.value = true
                _errorMessage.value = "Terjadi kesalahan saat menyimpan data pengguna."
                Log.e(TAG, "Error saving user: ${e.message}")
            }
        }
    }



    companion object {
        private const val TAG = "LoginViewModel"
    }
}
