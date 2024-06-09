package capstone.tim.aireal.api

import capstone.tim.aireal.response.CartRequest
import capstone.tim.aireal.response.CartResponse
import capstone.tim.aireal.response.DataLogin
import capstone.tim.aireal.response.DataRegister
import capstone.tim.aireal.response.DetailShopResponse
import capstone.tim.aireal.response.LoginResponse
import capstone.tim.aireal.response.ProductsResponse
import capstone.tim.aireal.response.regisbg.RegisterResponse
import capstone.tim.aireal.ui.toko.PenjualanViewModel
import capstone.tim.aireal.ui.toko.ProdukViewModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
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

    @GET("/api/shops/{shopId}")
    fun getShopDetails(
        @Header("Authorization") token: String,
        @Path("shopId") shopId: String
    ): Call<DetailShopResponse>

    @Headers("Content-Type: application/json")
    @POST("cart")
    fun addToCart(
        @Header("Authorization") token: String,
        @Body dataCart: CartRequest
    ): Call<CartResponse>


    @GET("penjualan") // Sesuaikan dengan endpoint API Anda
    fun getPenjualan(): Call<List<PenjualanViewModel.Penjualan>>

    @GET("produk") // Sesuaikan dengan endpoint API untuk mengambil data penjualan
    fun getProduk(): Call<ArrayList<ProdukViewModel.Product>>


}
