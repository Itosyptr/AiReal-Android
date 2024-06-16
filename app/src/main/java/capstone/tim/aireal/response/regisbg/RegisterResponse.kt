package capstone.tim.aireal.response.regisbg

data class RegisterResponse(
    val `data`: Data,
    val message: String,
    val status: String,
    val token: String
)