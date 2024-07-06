package com.example.pemerintahan.di

import android.content.Context
import com.example.pemerintahan.data.remote.UserRepository
import com.example.pemerintahan.data.local.datastore.UserPreferences
import com.example.pemerintahan.data.local.datastore.dataStore
import com.example.pemerintahan.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return UserRepository.getInstance(apiService, pref)
    }
}