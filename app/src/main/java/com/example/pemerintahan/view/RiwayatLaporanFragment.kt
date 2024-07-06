package com.example.pemerintahan.view

import HomeAdapter
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pemerintahan.data.remote.Result
import com.example.pemerintahan.viewmodel.RiwayatLaporanViewModel
import com.example.pemerintahan.viewmodel.ViewModelFactory
import com.example.pemerintahan.databinding.FragmentRiwayatLaporanBinding
import kotlinx.coroutines.launch

class RiwayatLaporanFragment : Fragment() {

    private var _binding: FragmentRiwayatLaporanBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<RiwayatLaporanViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRiwayatLaporanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listLaporan()
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvRiwayat.layoutManager = layoutManager
    }

    private fun listLaporan(){
        lifecycleScope.launch {
            viewModel.getRiwayatLaporan().observe(requireActivity()) { laporan ->
                when (laporan) {
                    is  Result.Error -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        val error = laporan.error
                        Toast.makeText(requireActivity(), error, Toast.LENGTH_SHORT).show()
                    }

                    is  Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        val adapter = HomeAdapter()
                        adapter.submitList(laporan.data)
                        binding.rvRiwayat.adapter = adapter
                    }
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
