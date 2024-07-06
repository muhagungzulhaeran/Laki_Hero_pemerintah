package com.example.pemerintahan.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pemerintahan.data.remote.UserRepository
import com.example.pemerintahan.data.local.datastore.UserModel

class SignInViewModel(private val repository: UserRepository): ViewModel() {
    fun signin(email: String, password: String) = repository.postLogin(email,password)
    suspend fun saveSession(userModel: UserModel){
        repository.saveSession(userModel)
    }
}