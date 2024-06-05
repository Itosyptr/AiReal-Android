package capstone.tim.aireal.ui.regis

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import capstone.tim.aireal.api.RetrofitClient
import capstone.tim.aireal.response.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    private val _registerResponse = MutableLiveData<RegisterResponse>()
    val getRegister: LiveData<RegisterResponse> = _registerResponse

    fun register(name: String, email: String, password: String) {
        RetrofitClient.apiInstance.register(name, email, password).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                _registerResponse.value = response.body()
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _registerResponse.value = RegisterResponse(true, "Gagal melakukan registrasi")
            }
        })
    }
}
