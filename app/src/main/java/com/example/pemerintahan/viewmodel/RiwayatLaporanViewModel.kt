package com.example.pemerintahan.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pemerintahan.data.remote.UserRepository

class RiwayatLaporanViewModel(private val repository: UserRepository) : ViewModel() {
    // TODO: Implement the ViewModel
    fun getRiwayatLaporan() = repository.getRiwayatLaporan()
}