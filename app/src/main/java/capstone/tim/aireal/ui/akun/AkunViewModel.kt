package capstone.tim.aireal.ui.akun

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AkunViewModel : ViewModel() {
    private val _profileImage = MutableLiveData<Int>()
    val profileImage: LiveData<Int> get() = _profileImage

    private val _profileName = MutableLiveData<String>()
    val profileName: LiveData<String> get() = _profileName

    fun updateProfile(imageResId: Int, name: String) {
        _profileImage.value = imageResId
        _profileName.value = name
    }
}
