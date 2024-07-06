package com.example.pemerintahan.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.pemerintahan.customview.EmailEditText
import com.example.pemerintahan.customview.PasswordEditText
import com.example.pemerintahan.data.remote.Result
import com.example.pemerintahan.viewmodel.SignUpViewModel
import com.example.pemerintahan.viewmodel.ViewModelFactory
import com.example.pemerintahan.databinding.ActivitySignUpBinding
import com.google.firebase.messaging.FirebaseMessaging

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var emailEditText: EmailEditText
    private lateinit var passwordEditText: PasswordEditText
    private val viewModel by viewModels<SignUpViewModel> {
        ViewModelFactory.getInstance(this)
    }

    object TokenData {
        var token_fcm: String? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        passwordEditText = binding.passwordEditText
        emailEditText = binding.emailEditText

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                // Send token to your server or use it directly in PHP script
                println("FCM Token: $token")
//                Toast.makeText(this, "Token" + token, Toast.LENGTH_LONG).show()
                // Store latitude and longitude in public variables
                TokenData.token_fcm = token
            }
        }

        viewSetup()
        onClickRegister()
        binding.signintv.setOnClickListener { signIn() }
    }
    private fun viewSetup() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
    private fun onClickRegister(){
        binding.signupButton.setOnClickListener {
            val nama = binding.namaEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val token_fcm = TokenData.token_fcm.toString()

            viewModel.postSignup(nama, email, password, token_fcm).observe(this@SignUpActivity){ user ->
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
    }
    private fun signIn(){
        val signUpintent = Intent(this, SignInActivity::class.java)
        startActivity(signUpintent)
        finish()
    }
}