package capstone.tim.aireal

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import capstone.tim.aireal.data.pref.UserModel
import capstone.tim.aireal.data.pref.UserPreference

class MainViewModel(private val pref: UserPreference) : ViewModel() {

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }
}

