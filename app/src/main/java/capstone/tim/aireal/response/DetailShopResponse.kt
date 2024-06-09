package capstone.tim.aireal.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailShopResponse(

    @field:SerializedName("data")
    val data: ShopData? = null,

    @field:SerializedName("status")
    val status: String? = null
) : Parcelable

@Parcelize
data class ShopData(

    @field:SerializedName("createdAt")
    val createdAt: CreatedAt? = null,

    @field:SerializedName("province")
    val province: String? = null,

    @field:SerializedName("city")
    val city: String? = null,

    @field:SerializedName("street")
    val street: String? = null,

    @field:SerializedName("image_url")
    val imageUrl: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("userId")
    val userId: String? = null,

    @field:SerializedName("updatedAt")
    val updatedAt: UpdatedAt? = null
) : Parcelable


