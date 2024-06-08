package capstone.tim.aireal.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse (
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String?
    )

data class DataRegister(
    val name: String,
    val email: String,
    val password: String
)