package com.example.pemerintahan.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.pemerintahan.R
import com.example.pemerintahan.data.remote.Result
import com.example.pemerintahan.databinding.FragmentTrainingRecommendationBinding
import com.example.pemerintahan.viewmodel.TrainingRecommendationViewModel
import com.example.pemerintahan.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class TrainingRecommendationFragment : Fragment() {

    private var _binding: FragmentTrainingRecommendationBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<TrainingRecommendationViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrainingRecommendationBinding.inflate(inflater, container, false)

        // Initialize the adapters for the autoCompleteTextViews
        val jenisKelamin = resources.getStringArray(R.array.jenis_kelamin)
        val arrayAdapterKelamin = ArrayAdapter(requireContext(), R.layout.dropdown, jenisKelamin)
        binding.autoCompleteText1.setAdapter(arrayAdapterKelamin)

        val pendidikan = resources.getStringArray(R.array.pendidikan)
        val arrayAdapterPendidikan = ArrayAdapter(requireContext(), R.layout.dropdown, pendidikan)
        binding.autoCompleteText2.setAdapter(arrayAdapterPendidikan)

        val hobby = resources.getStringArray(R.array.hobby)
        val arrayAdapterHobby = ArrayAdapter(requireContext(), R.layout.dropdown, hobby)
        binding.autoCompleteText3.setAdapter(arrayAdapterHobby)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.submitButton.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Konfirmasi")
                .setMessage("Apakah Anda yakin ingin melihat rekomendasi berbasis AI?")
                .setPositiveButton("Ya") { _, _ ->
                    submitData()
                }
                .setNegativeButton("Tidak") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun submitData() {

        val name = binding.edName.text.toString()
        val age = binding.edAge.text.toString().toIntOrNull()
        val sex = binding.autoCompleteText1.text.toString()
        val education = binding.autoCompleteText2.text.toString()
        val hobby = binding.autoCompleteText3.text.toString()

        if (age == null || sex.isEmpty() || education.isEmpty() || hobby.isEmpty()) {
            Toast.makeText(requireContext(), "Mohon dimasukkan semua data", Toast.LENGTH_SHORT)
                .show()
            return
        }

        lifecycleScope.launch {
            viewModel.recommendation(age, sex, education, hobby)
                .observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is Result.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT)
                                .show()
                        }

                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                requireContext(),
                                "Data submitted successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            // Navigate to another activity if needed
                            binding.hasilTr.text =
                                "Anda, " + name + " direkomendasikan : " + result.data.message.toString()
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
