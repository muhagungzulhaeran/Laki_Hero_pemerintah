package com.example.pemerintahan.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.example.pemerintahan.R
import com.example.pemerintahan.data.remote.Result
import com.example.pemerintahan.data.remote.UserRepository
import com.example.pemerintahan.data.remote.response.ListLaporanSayaItem
import com.example.pemerintahan.data.remote.retrofit.ApiService
import com.example.pemerintahan.databinding.ActivityDetailLaporanBinding
import com.example.pemerintahan.view.DetailLaporanActivity.LocationData.status
import com.example.pemerintahan.viewmodel.ActionLaporViewModel
import com.example.pemerintahan.viewmodel.SignUpViewModel
import com.example.pemerintahan.viewmodel.ViewModelFactory

@Suppress("DEPRECATION")
class DetailLaporanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailLaporanBinding
    private val viewModel by viewModels<ActionLaporViewModel> {
        ViewModelFactory.getInstance(this)
    }
    object LocationData {
        var latitudes: Double? = null
        var longitudes: Double? = null
        var email: String? = null
        var status: String? = null
        var IdLaporan: Int? = null

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailLaporanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detail = intent.getParcelableExtra<ListLaporanSayaItem>(DETAIL_LAPORAN) as ListLaporanSayaItem
        laporanDetail(detail)

        val latitudes = LocationData.latitudes
        val longitudes = LocationData.longitudes

        if (status=="monitored"){
            binding.tolakbtn.visibility = View.GONE // or View.INVISIBLE
        }
        else if (status=="rejected" || status=="approved"){
            binding.tolakbtn.visibility = View.GONE // or View.INVISIBLE
            binding.terimabtn.visibility = View.GONE // or View.INVISIBLE
        }
        binding.locationBtn.setOnClickListener {
            val mapIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q=$latitudes,$longitudes")
            )
            mapIntent.setPackage("com.google.android.apps.maps")

            if (mapIntent.resolveActivity(packageManager) != null) {
                startActivity(mapIntent)
            } else {
                Toast.makeText(this, "Tidak ada google maps", Toast.LENGTH_LONG).show()
            }
        }



        binding.terimabtn.setOnClickListener {

            val email = LocationData.email
            val id_laporan = LocationData.IdLaporan

            if (status=="monitored"){
                status = "approved"
            }
            else{
                status = "monitored"
            }



            AlertDialog.Builder(this)
                .setTitle("Konfirmasi")
                .setMessage("Apakah Anda yakin ingin menerima laporan ini?")
                .setPositiveButton("Ya") { _, _ ->
//            Toast.makeText(this, "email" + email, Toast.LENGTH_LONG).show()
//            Toast.makeText(this, "id laporan " + id_laporan, Toast.LENGTH_LONG).show()
//            Toast.makeText(this, "status" +status, Toast.LENGTH_LONG).show()

            viewModel.actionLaporan(email.toString(), id_laporan.toString(), status.toString()).observe(this@DetailLaporanActivity){ user ->
                when (user) {
                    is Result.Error -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        val error = user.error
                        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                    }

                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
        .setNegativeButton("Tidak") { dialog, which ->
            dialog.dismiss()
        }
        .show()


        }

        binding.tolakbtn.setOnClickListener {

            val email = LocationData.email
            val id_laporan = LocationData.IdLaporan
            val status = "rejected"
            AlertDialog.Builder(this)
                .setTitle("Konfirmasi")
                .setMessage("Apakah Anda yakin ingin menolak laporan ini?")
                .setPositiveButton("Ya") { _, _ ->
//            Toast.makeText(this, "email" + email, Toast.LENGTH_LONG).show()
//            Toast.makeText(this, "id laporan " + id_laporan, Toast.LENGTH_LONG).show()
//            Toast.makeText(this, "status" +status, Toast.LENGTH_LONG).show()

                    viewModel.actionLaporan(email.toString(), id_laporan.toString(), status).observe(this@DetailLaporanActivity){ user ->
                        when (user) {
                            is Result.Error -> {
                                binding.progressBar.visibility = View.INVISIBLE
                                val error = user.error
                                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                            }

                            is Result.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }

                            is Result.Success -> {
                                binding.progressBar.visibility = View.GONE
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
                }
                .setNegativeButton("Tidak") { dialog, which ->
                    dialog.dismiss()
                }
                .show()


        }



        setActionBar()
    }

    private fun setActionBar(){
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Detail laporan"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun laporanDetail(detail: ListLaporanSayaItem){
        Glide.with(applicationContext)
            .load(detail.gambar)
            .into(binding.ivDetailPhoto)
        binding.pelaporTv.text = "Pelapor : ${detail.email}"
        binding.namaTempat.text = detail.tempat
        binding.tanggalTv.text = detail.createdAt
        binding.isiDeskripsi.text = detail.deskripsi
        when (detail.status.toLowerCase()) {
            "monitored" -> binding.cvStatus.setBackgroundResource(R.drawable.background_monitored)
            "approved" -> binding.cvStatus.setBackgroundResource(R.drawable.background_approved)
            "rejected" -> binding.cvStatus.setBackgroundResource(R.drawable.background_rejected)
            else -> binding.cvStatus.setBackgroundResource(R.drawable.background_abu2)
        }
        binding.isiStatus.text = detail.status
        // Store latitude and longitude in public variables
        LocationData.latitudes  = detail.latitude
        LocationData.longitudes = detail.longitude

        LocationData.IdLaporan = detail.id
        LocationData.status    = detail.status
        LocationData.email     = detail.email

    }

//    private fun onClickRegister(){
//        binding.tolakbtn.setOnClickListener {
//            val email = binding.emailEditText.text.toString()
//            val password = binding.passwordEditText.text.toString()
//
//            viewModel.postSignup(nama, email, password).observe(this@SignUpActivity){ user ->
//                when (user) {
//                    is Result.Error -> {
//                        binding.progressBar.visibility = View.INVISIBLE
//                        val error = user.error
//                        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
//                    }
//
//                    is Result.Loading -> {
//                        binding.progressBar.visibility = View.VISIBLE
//                    }
//
//                    is Result.Success -> {
//                        binding.progressBar.visibility = View.GONE
//                        val intent = Intent(this, MainActivity::class.java)
//                        startActivity(intent)
//                        finish()
//                    }
//                }
//            }
//        }
//    }

    companion object {
        const val DETAIL_LAPORAN = "detail_laporan"
    }
}