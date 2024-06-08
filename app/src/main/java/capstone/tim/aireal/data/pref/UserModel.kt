package capstone.tim.aireal.data.pref

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val name: String = "",
    val email: String = "email",
    val userId: String = "",
    val token: String = "",
    val isLogin: Boolean = false
) : Parcelable
