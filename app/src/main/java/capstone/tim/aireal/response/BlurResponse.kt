package capstone.tim.aireal.response

data class BlurResponse(
    val is_blurry: Boolean,
    val percentage: String,
    val prediction: List<List<Double>>
)