package capstone.tim.aireal.ui.home

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Categories(
    val photo: Int,
    val name: String
) : Parcelable
