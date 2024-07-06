package com.example.pemerintahan.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.pemerintahan.data.local.datastore.UserModel
import com.example.pemerintahan.data.remote.UserRepository

class ProfileMenuViewModel(private val repository: UserRepository): ViewModel() {
    suspend fun logout(){
        repository.logout()
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}