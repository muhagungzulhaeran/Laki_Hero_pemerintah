package com.example.pemerintahan.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pemerintahan.customview.EmailEditText
import com.example.pemerintahan.customview.PasswordEditText
import com.example.pemerintahan.data.local.datastore.UserModel
import com.example.pemerintahan.data.remote.Result
import com.example.pemerintahan.viewmodel.SignInViewModel
import com.example.pemerintahan.viewmodel.ViewModelFactory
import com.example.pemerintahan.databinding.ActivitySignInBinding
import kotlinx.coroutines.launch

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var emailEditText: EmailEditText
    private lateinit var passwordEditText: PasswordEditText
    private val viewModel by viewModels<SignInViewModel> {
        ViewModelFactory.getInstance(this)
    }
    // variable for shared preferences.
    private lateinit var sharedpreferences: SharedPreferences
    private var emailsp: String? = null
    private var passwordsp: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        emailEditText = binding.emailEditText
        passwordEditText = binding.passwordEditText

        // getting the data which is stored in shared preferences.
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        // in shared prefs inside get string method
        // we are passing key value as EMAIL_KEY and
        // default value is
        // set to null if not present.
        emailsp = sharedpreferences.getString("EMAIL_KEY", null)
        passwordsp = sharedpreferences.getString("PASSWORD_KEY", null)

        setupView()
        onClickSignIn()
        binding.signUptv.setOnClickListener { signUp() }
    }

    private fun setupView() {
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

    private fun onClickSignIn() {
        binding.signinButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val editor = sharedpreferences.edit()

            // below two lines will put values for
            // email and password in shared preferences.
            editor.putString(EMAIL_KEY, email)
            editor.putString(PASSWORD_KEY, password)

            // to save our data with key and value.
            editor.apply()

            viewModel.signin(email, password).observe(this@SignInActivity) { user ->
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
                        binding.progressBar.visibility = View.INVISIBLE
                        saveSession(
                            UserModel(
                                user.data.loginResult.token,
                                user.data.loginResult.name,
                                user.data.loginResult.userId,
                                true
                            ))
                        val intent = Intent(this@SignInActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }
    private fun signUp(){
        val signUpintent = Intent(this, SignUpActivity::class.java)
        startActivity(signUpintent)
        finish()
    }
    private fun saveSession(session: UserModel){
        lifecycleScope.launch {
            viewModel.saveSession(session)
            val intent = Intent(this@SignInActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            ViewModelFactory.clearInstance()
            startActivity(intent)
        }
    }

    // creating constant keys for shared preferences.
    companion object {
        const val SHARED_PREFS = "shared_prefs"
        const val EMAIL_KEY = "email_key"
        const val PASSWORD_KEY = "password_key"
    }
}