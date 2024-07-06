package com.example.pemerintahan.view

import HomeAdapter
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pemerintahan.R
import com.example.pemerintahan.data.remote.Result
import com.example.pemerintahan.databinding.FragmentHomeBinding
import com.example.pemerintahan.viewmodel.HomeViewModel
import com.example.pemerintahan.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }
//    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupListeners()
        observeLaporan()
    }

    private fun setupUI() {
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerView.layoutManager = layoutManager
    }

    private fun setupListeners() {
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.profile -> {
                    startActivity(Intent(requireActivity(), ProfileMenuActivity::class.java))
                    true
                }
                else -> false
            }
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            observeLaporan() // Refresh data when user swipes down
        }
    }

    private fun observeLaporan() {
        lifecycleScope.launch {
            viewModel.getLaporanSaya().observe(viewLifecycleOwner) { laporan ->
                when (laporan) {
                    is Result.Error -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.swipeRefreshLayout.isRefreshing = false
                        val error = laporan.error
                        Toast.makeText(requireActivity(), error, Toast.LENGTH_SHORT).show()
                    }
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.swipeRefreshLayout.isRefreshing = false
                        val adapter = HomeAdapter()
                        adapter.submitList(laporan.data)
                        binding.recyclerView.adapter = adapter
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
