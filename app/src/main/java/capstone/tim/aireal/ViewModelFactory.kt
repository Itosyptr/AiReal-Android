package capstone.tim.aireal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import capstone.tim.aireal.data.lib.pref.UserPreference
import capstone.tim.aireal.ui.login.LoginViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val pref: UserPreference) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}