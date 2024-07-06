package com.example.pemerintahan.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pemerintahan.data.remote.UserRepository

class TrainingRecommendationViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun recommendation(umur : Int, jenisKelamin: String, pendidikan: String, hobby: String) =
        userRepository.recommendation(umur, jenisKelamin, pendidikan, hobby)
}