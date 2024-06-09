package capstone.tim.aireal

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import capstone.tim.aireal.data.pref.UserPreference
import capstone.tim.aireal.ui.detailProduct.DetailProductViewModel
import capstone.tim.aireal.ui.explore.ExploreViewModel
import capstone.tim.aireal.ui.home.HomeViewModel
import capstone.tim.aireal.ui.login.LoginViewModel

class ViewModelFactory(private val pref: UserPreference, private val context: Context) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref, context) as T
            }

            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(pref, context) as T
            }

            modelClass.isAssignableFrom(DetailProductViewModel::class.java) -> {
                DetailProductViewModel(pref, context) as T
            }

            modelClass.isAssignableFrom(ExploreViewModel::class.java) -> {
                ExploreViewModel(pref, context) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}
