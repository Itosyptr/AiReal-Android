package capstone.tim.aireal.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class CartRequest(

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("items")
	val items: List<ItemsItem?>? = null
) : Parcelable

@Parcelize
data class ItemsItem(

	@field:SerializedName("quantity")
	val quantity: Int? = null,

	@field:SerializedName("productId")
	val productId: String? = null
) : Parcelable
