package capstone.tim.aireal.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserOrderResponse(

    @field:SerializedName("data")
    val data: List<OrderItem?>? = null,

    @field:SerializedName("status")
    val status: String? = null
) : Parcelable

@Parcelize
data class OrderItem(

    @field:SerializedName("createdAt")
    val createdAt: CreatedAt? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("userId")
    val userId: String? = null,

    @field:SerializedName("items")
    val items: List<DetailItem?>? = null
) : Parcelable

@Parcelize
data class DetailItem(

    @field:SerializedName("quantity")
    val quantity: Int? = null,

    @field:SerializedName("productId")
    val productId: String? = null
) : Parcelable
