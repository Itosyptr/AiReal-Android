package capstone.tim.aireal.ui.toko

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import capstone.tim.aireal.R

class TokoViewModel : ViewModel() {

    private val _profileImage = MutableLiveData<Int>().apply {
        value = R.drawable.logo // Default image
    }
    val profileImage: LiveData<Int> = _profileImage

    private val _profileName = MutableLiveData<String>().apply {
        value = "Nama Toko" // Default name
    }
    val profileName: LiveData<String> = _profileName

    fun updateProfile(imageResId: Int, name: String) {
        _profileImage.value = imageResId
        _profileName.value = name
    }
}
