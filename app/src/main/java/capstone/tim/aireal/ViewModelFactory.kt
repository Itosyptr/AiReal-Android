package capstone.tim.aireal

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import capstone.tim.aireal.data.pref.UserPreference
import capstone.tim.aireal.ui.login.LoginViewModel

class ViewModelFactory(private val pref: UserPreference, private val context: Context) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref, context) as T
            }
            // ... (viewmodel lainnya)
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}
