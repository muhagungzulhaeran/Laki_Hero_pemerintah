package com.example.pemerintahan.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.pemerintahan.data.local.datastore.UserModel
import com.example.pemerintahan.data.local.datastore.UserPreferences
import com.example.pemerintahan.data.remote.response.ErrorResponse
import com.example.pemerintahan.data.remote.response.ListLaporanSayaItem
import com.example.pemerintahan.data.remote.response.LoginResponse
import com.example.pemerintahan.data.remote.response.RegisterResponse
import com.example.pemerintahan.data.remote.response.UploadResponse
import com.example.pemerintahan.data.remote.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class UserRepository (
    private val apiService: ApiService,
    private val userPreferences: UserPreferences
){
    fun postRegister(nama: String, email: String, password: String,  token_fcm: String): LiveData<Result<RegisterResponse>> = liveData{
        emit(Result.Loading)
        try {
            val response = apiService.register(nama, email, password, token_fcm)
            emit(Result.Success(response))
        } catch (e: HttpException){
            val error = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(error, ErrorResponse::class.java)
            emit(Result.Error(errorBody.message))
        }
    }
    fun postLogin(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            emit(Result.Success(response))
        } catch (e:HttpException){
            val error = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(error, ErrorResponse::class.java)
            emit(Result.Error(errorBody.message))
        }
    }

    fun recommendation(umur: Int, jenisKelamin: String, pendidikan: String, hobby: String): LiveData<Result<UploadResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.recommendation(umur, jenisKelamin, pendidikan, hobby)
            emit(Result.Success(response))
        } catch (e:HttpException){
            val error = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(error, ErrorResponse::class.java)
            emit(Result.Error(errorBody.message))
        }
    }

    fun getLaporanSaya(): LiveData<Result<List<ListLaporanSayaItem>>> = liveData{
        emit(Result.Loading)
        try{
            val response = apiService.getlaporanSaya()
            emit(Result.Success(response.listLaporanSaya))
        }catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(Result.Error(errorResponse.message))
        }catch (e: Exception){
            emit(Result.Error(e.message ?: "Error"))
        }
    }

    fun actionLaporan(email: String, idLaporan: String, status: String): LiveData<Result<UploadResponse>> = liveData{
        emit(Result.Loading)
        val requestBodyEmail = email.toRequestBody("text/plain".toMediaType())
        val requestBodyIdLaporan = idLaporan.toRequestBody("text/plain".toMediaType())
        val requestBodyStatus = status.toRequestBody("text/plain".toMediaType())
        try {
            val successResponse = apiService.actionLaporan(requestBodyEmail, requestBodyIdLaporan, requestBodyStatus)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, UploadResponse::class.java)
            emit(Result.Error(errorResponse.message))
        }
    }

    fun getRiwayatLaporan(): LiveData<Result<List<ListLaporanSayaItem>>> = liveData{
        emit(Result.Loading)
        try{
            val response = apiService.getRiwayatLaporan()
            emit(Result.Success(response.listLaporanSaya))
        }catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(Result.Error(errorResponse.message))
        }catch (e: Exception){
            emit(Result.Error(e.message ?: "Error"))
        }
    }

    suspend fun saveSession(userModel: UserModel){
        userPreferences.saveSession(userModel)
    }
    fun getSession(): Flow<UserModel> {
        return userPreferences.getSession()
    }
    suspend fun logout(){
        userPreferences.logout()
    }
    companion object {
        private var INSTANCE: UserRepository? = null
        fun clearInstance(){
            INSTANCE = null
        }
        fun getInstance(
            apiService: ApiService,
            userPreferences: UserPreferences
        ): UserRepository =
            INSTANCE ?: synchronized(this){
                INSTANCE ?: UserRepository(apiService, userPreferences)
        }.also { INSTANCE = it }
    }
}