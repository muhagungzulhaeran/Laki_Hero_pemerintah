package com.example.pemerintahan.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.pemerintahan.databinding.ActivitySplashBinding
import com.example.pemerintahan.viewmodel.MainViewModel
import com.example.pemerintahan.viewmodel.ViewModelFactory

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this){
            Handler(Looper.getMainLooper()).postDelayed({
                try {
                    val intent: Intent = if (it.email!== "") {
                        Intent(this, MainActivity::class.java)
                    } else {
                        Intent(this, SignInActivity::class.java)
                    }
                    startActivity(intent)
                    finish()
                } catch (e: Exception) {
                    Log.e("ERROR", e.message.toString())
                }
            }, DELAY_TIME)
        }
    }
    companion object {
        const val DELAY_TIME: Long = 1_000
    }
}