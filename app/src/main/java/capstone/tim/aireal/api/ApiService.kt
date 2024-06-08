package capstone.tim.aireal.api

import capstone.tim.aireal.response.LoginResponse
import capstone.tim.aireal.response.ProductsResponse
import capstone.tim.aireal.response.RegisterResponse
import capstone.tim.aireal.ui.toko.PenjualanViewModel
import capstone.tim.aireal.ui.toko.ProdukViewModel
import capstone.tim.aireal.ui.toko.TokoAdapter
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST


interface ApiService {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String?,
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Call<RegisterResponse>

    @GET("products")
    fun getProduct(): Call<ProductsResponse>

    @GET("penjualan") // Sesuaikan dengan endpoint API Anda
    fun getPenjualan(): Call<List<PenjualanViewModel.Penjualan>>

    @GET("produk") // Sesuaikan dengan endpoint API untuk mengambil data penjualan
    fun getProduk(): Call<ArrayList<ProdukViewModel.Product>>
}
