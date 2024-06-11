package capstone.tim.aireal.api

import capstone.tim.aireal.response.CartRequest
import capstone.tim.aireal.response.CartResponse
import capstone.tim.aireal.response.DataLogin
import capstone.tim.aireal.response.DataRegister
import capstone.tim.aireal.response.DetailShopResponse
import capstone.tim.aireal.response.EditProfileResponse
import capstone.tim.aireal.response.LoginResponse
import capstone.tim.aireal.response.ProductByIdResponse
import capstone.tim.aireal.response.ProductsResponse
import capstone.tim.aireal.response.UserOrderResponse
import capstone.tim.aireal.response.regisbg.RegisterResponse
import capstone.tim.aireal.ui.toko.PenjualanViewModel
import capstone.tim.aireal.ui.toko.ProdukViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("login") // Sesuaikan dengan endpoint login Anda
    fun login(@Body dataLogin: DataLogin): Call<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST("register") // Sesuaikan dengan endpoint login Anda
    suspend fun register(@Body registerDataRegister: DataRegister): Response<RegisterResponse>

    @GET("products")
    fun getProduct(
        @Header("Authorization") token: String,
    ): Call<ProductsResponse>

    @GET("products")
    fun getProductbyCategory(
        @Header("Authorization") token: String,
        @Query("category") category: String,
    ): Call<ProductsResponse>

    @GET("shops/{shopId}")
    fun getShopDetails(
        @Header("Authorization") token: String,
        @Path("shopId") shopId: String
    ): Call<DetailShopResponse>

    @GET("products/{id}")
    fun getProductDetails(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<ProductByIdResponse>

    @Headers("Content-Type: application/json")
    @POST("cart")
    fun addToCart(
        @Header("Authorization") token: String,
        @Body dataCart: CartRequest
    ): Call<CartResponse>

    @GET("cart")
    fun getCart(
        @Header("Authorization") token: String,
        @Query("userId") userId: String,
    ): Call<CartResponse>

    @GET("order/")
    fun getUserOrder(
        @Header("Authorization") token: String,
        @Query("userId") userId: String,
    ): Call<UserOrderResponse>

    @Multipart
    @PUT("users/{userId}")
    fun updateUser(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("username") username: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("address") address: RequestBody,
        @Part("phone_number") phoneNumber: RequestBody,
        @Part image: MultipartBody.Part,
    ): Call<EditProfileResponse>

    @GET("penjualan") // Sesuaikan dengan endpoint API Anda
    fun getPenjualan(): Call<List<PenjualanViewModel.Penjualan>>

    @GET("produk") // Sesuaikan dengan endpoint API untuk mengambil data penjualan
    fun getProduk(): Call<ArrayList<ProdukViewModel.Product>>
}
