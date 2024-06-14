package capstone.tim.aireal.api

import capstone.tim.aireal.response.CartRequest
import capstone.tim.aireal.response.CartResponse
import capstone.tim.aireal.response.CreateProductResponse
import capstone.tim.aireal.response.DataLogin
import capstone.tim.aireal.response.DataRegister
import capstone.tim.aireal.response.DetailShopResponse
import capstone.tim.aireal.response.EditProfileResponse
import capstone.tim.aireal.response.EditShopResponse
import capstone.tim.aireal.response.LoginResponse
import capstone.tim.aireal.response.OrderResponse
import capstone.tim.aireal.response.ProductByIdResponse
import capstone.tim.aireal.response.ProductsResponse
import capstone.tim.aireal.response.UserOrderResponse
import capstone.tim.aireal.response.UserProfileResponse
import capstone.tim.aireal.response.dataOrder
import capstone.tim.aireal.response.regisbg.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
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
    @POST("login")
    fun login(@Body dataLogin: DataLogin): Call<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST("register")
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

    @GET("products")
    fun getProductbyName(
        @Header("Authorization") token: String,
        @Query("name") name: String,
    ): Call<ProductsResponse>

    @GET("products/shop/{shopId}")
    fun getProductbyShopId(
        @Header("Authorization") token: String,
        @Path("shopId") shopId: String,
    ): Call<ProductsResponse>

    @GET("shops/{shopId}")
    fun getShopDetails(
        @Header("Authorization") token: String,
        @Path("shopId") shopId: String
    ): Call<DetailShopResponse>

    @GET("shops/user/{userId}")
    fun getShopbyUserId(
        @Header("Authorization") token: String,
        @Path("userId") shopId: String
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

    @Multipart
    @PUT("shops/{shopId}")
    fun updateShop(
        @Header("Authorization") token: String,
        @Path("shopId") shopId: String,
        @Part("userId") userId: RequestBody,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("street") street: RequestBody,
        @Part("city") city: RequestBody,
        @Part("province") gender: RequestBody,
        @Part image: MultipartBody.Part,
    ): Call<EditShopResponse>

    @POST("products")
    fun addProduct(
        @Header("Authorization") token: String,
        @Part("shopId") shopId: RequestBody,
        @Part("categoryId") categoryId: RequestBody,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("longdescription") longdescription: RequestBody,
        @Part("price") price: RequestBody,
        @Part("stock") stock: RequestBody,
        @Part image: List<MultipartBody.Part>,
    ): Call<CreateProductResponse>

    @GET("users/{userId}")
    fun getUserProfile(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): Call<UserProfileResponse>

    @Headers("Content-Type: application/json")
    @POST("order")
    fun createOrder(
        @Header("Authorization") token: String,
        @Body dataOrder: dataOrder
    ): Call<OrderResponse>
}
