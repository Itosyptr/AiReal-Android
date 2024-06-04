package capstone.tim.aireal.data.pref

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    var token: String,
    var isLogin: Boolean
): Parcelable