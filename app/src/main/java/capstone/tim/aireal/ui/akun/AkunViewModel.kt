package capstone.tim.aireal.ui.akun


import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import capstone.tim.aireal.api.ApiConfig
import capstone.tim.aireal.data.pref.UserModel
import capstone.tim.aireal.data.pref.UserPreference
import capstone.tim.aireal.response.DataUser
import capstone.tim.aireal.response.UserProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AkunViewModel(
    private val pref: UserPreference, // Tambahkan UserPreference
    private val context: Context    // Tambahkan Context
) : ViewModel() {
    private val _userProfile = MutableLiveData<DataUser?>()
    val userProfile: LiveData<DataUser?> get() = _userProfile
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage
    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    companion object {
        private const val TAG = "AkunViewModel"
    }

    fun fetchUserProfile(token: String, userId: String) { // Pindahkan fungsi ini ke bawah
        _isLoading.value = true


        val client = ApiConfig.getApiService().getUserProfile(token, userId) // Pastikan method GET
        client.enqueue(object : Callback<UserProfileResponse> {
            override fun onResponse(
                call: Call<UserProfileResponse>,
                response: Response<UserProfileResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse: ${response.body()?.data}")
                    _userProfile.value = response.body()?.data
                } else {
                    _isError.value = true
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })

    }

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }
}

