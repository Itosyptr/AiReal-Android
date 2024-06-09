package capstone.tim.aireal.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductsResponse(
    val data: List<DataItem?>? = null,
    val status: String? = null
) : Parcelable

@Parcelize
data class UpdatedAt(
    val nanoseconds: Int? = null,
    val seconds: Int? = null
) : Parcelable

@Parcelize
data class DataItem(
    val createdAt: CreatedAt? = null,
    val price: String? = null,

    @field:SerializedName("image_url")
    val imageUrl: List<String?>? = null,
    val longdescription: String? = null,
    val name: String? = null,
    val description: String? = null,
    val id: String? = null,
    val shopId: String? = null,
    val stock: String? = null,
    val categoryId: String? = null,
    val updatedAt: UpdatedAt? = null,
    val location: String? = null,
    val imageShop: String? = null,
    val shopName: String? = null
) : Parcelable

@Parcelize
data class CreatedAt(
    val nanoseconds: Int? = null,
    val seconds: Int? = null
) : Parcelable
