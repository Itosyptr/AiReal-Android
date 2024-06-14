package capstone.tim.aireal.response

import com.google.gson.annotations.SerializedName

data class OrderResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class dataOrder (
	val userId: String
)

