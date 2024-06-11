package capstone.tim.aireal.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class CreateProductResponse(

	@field:SerializedName("data")
	val data: ProductData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable

@Parcelize
data class ProductData(

	@field:SerializedName("price")
	val price: String? = null,

	@field:SerializedName("image_url")
	val imageUrl: List<String?>? = null,

	@field:SerializedName("longdescription")
	val longdescription: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("shopId")
	val shopId: String? = null,

	@field:SerializedName("stock")
	val stock: String? = null,

	@field:SerializedName("categoryId")
	val categoryId: String? = null
) : Parcelable
