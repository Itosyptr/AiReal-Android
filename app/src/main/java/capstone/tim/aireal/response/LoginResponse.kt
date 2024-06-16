package capstone.tim.aireal.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class LoginResponse(
	val data: Data? = null,
	val status: String? = null,
	val token: String? = null,
	val message: String?,
	val error: Boolean
) : Parcelable

@Parcelize
data class Data(
	val createdAt: CreatedAt? = null,
	val password: String? = null,
	val address: String? = null,
	val gender: String? = null,
	val imageUrl: List<String?>? = null,
	val name: String? = null,
	val phoneNumber: String? = null,
	val id: String? = null,
	val email: String? = null,
	val username: String? = null,
	val updatedAt: UpdatedAt? = null
) : Parcelable

data class DataLogin(
	val email: String,
	val password: String
)
