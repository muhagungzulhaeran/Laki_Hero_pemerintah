package com.example.pemerintahan.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pemerintahan.data.remote.UserRepository

class HomeViewModel(private val repository: UserRepository) : ViewModel() {

    fun getLaporanSaya() = repository.getLaporanSaya()
}