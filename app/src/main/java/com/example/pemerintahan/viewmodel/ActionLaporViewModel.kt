package com.example.pemerintahan.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pemerintahan.data.remote.UserRepository

class ActionLaporViewModel (private val userRepository: UserRepository): ViewModel() {
    fun actionLaporan(email : String, id_laporan: String, status: String) =
        userRepository.actionLaporan(email, id_laporan, status)
}