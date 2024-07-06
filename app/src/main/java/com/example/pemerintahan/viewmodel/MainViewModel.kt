package com.example.pemerintahan.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.pemerintahan.data.remote.UserRepository
import com.example.pemerintahan.data.local.datastore.UserModel

class MainViewModel(private val repository: UserRepository): ViewModel() {
//    fun getStories() = repository.getStory()

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}