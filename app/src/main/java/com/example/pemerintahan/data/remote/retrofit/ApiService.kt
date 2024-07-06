package com.example.pemerintahan.data.remote.retrofit

import com.example.pemerintahan.data.remote.response.LaporanResponse
import com.example.pemerintahan.data.remote.response.LoginResponse
import com.example.pemerintahan.data.remote.response.RegisterResponse
import com.example.pemerintahan.data.remote.response.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @FormUrlEncoded
    @POST("register_admin")
    suspend fun register(
        @Field("nama")nama: String,
        @Field("email")email: String,
        @Field("password")password: String,
        @Field("token_fcm")token_fcm: String
    ):RegisterResponse

    @FormUrlEncoded
    @POST("login_admin")
    suspend fun login(
        @Field("email") email: String,
        @Field("password")password: String
    ):LoginResponse

    @GET("api_pem_select_laporan.php")
    suspend fun getlaporanSaya(
    ): LaporanResponse

    @GET("api_pem_select_laporan_proses.php")
    suspend fun getRiwayatLaporan(
    ): LaporanResponse

    @Multipart
    @POST("tambah_laporan")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part ("email")email: RequestBody,
        @Part("tempat") tempat: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody
    ): UploadResponse

    @Multipart
    @POST("api_pem_execute_laporan.php")
    suspend fun actionLaporan(
        @Part ("email")email: RequestBody,
        @Part("id_laporan") idLaporan: RequestBody,
        @Part("status") status: RequestBody,
    ): UploadResponse

    @FormUrlEncoded
    @POST("api_training_recomendation.php")
    suspend fun recommendation(
        @Field ("umur")umur: Int,
        @Field("jenis_kelamin") jenisKelamin: String,
        @Field("pendidikan") pendidikan: String,
        @Field("hobby") hobby: String,
    ): UploadResponse
}