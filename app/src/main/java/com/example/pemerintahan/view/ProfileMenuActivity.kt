package com.example.pemerintahan.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import android.window.SplashScreen
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import com.example.pemerintahan.viewmodel.ProfileMenuViewModel
import com.example.pemerintahan.viewmodel.ViewModelFactory
import com.example.pemerintahan.R
import com.example.pemerintahan.databinding.ActivityProfileMenuBinding
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class ProfileMenuActivity : AppCompatActivity() {
    private lateinit var sharedpreferences: SharedPreferences
    private var emailsp: String? = null

    private val viewModel by viewModels<ProfileMenuViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityProfileMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActionBar()

        // Initializing shared preferences
        sharedpreferences = this.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        emailsp = sharedpreferences.getString(EMAIL_KEY, null)


        viewModel.getSession().observe(this){
            binding.emailTv.setText(emailsp)
            binding.namaTv.setText((it.email))
        }

        binding.logoutBtn.setOnClickListener { onClicklogout() }
    }

    private fun setActionBar(){
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Profil"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun onClicklogout(){
        lifecycleScope.launch {
            viewModel.logout()
            val intent = Intent(this@ProfileMenuActivity, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    companion object {
        const val SHARED_PREFS = "shared_prefs"
        const val EMAIL_KEY = "email_key"
    }
}